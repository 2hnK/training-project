# Micrometer 메트릭 조합 사용 가이드

## 개요

실무에서는 Timer, Counter, Gauge를 **조합하여 사용**하는 것이 일반적입니다. 각 메트릭은 서로 다른 관점에서 시스템을 측정하므로, 함께 사용하면 더 풍부한 모니터링 데이터를 얻을 수 있습니다.

## 메트릭 타입별 특징

| 메트릭 타입 | 측정 대상 | 자동 제공 메트릭 | 주요 용도 |
|---------|--------|--------------|---------|
| **Timer** | 실행 시간 + 호출 횟수 | count, sum, max, mean | 성능 모니터링, 응답 시간 추적 |
| **Counter** | 이벤트 발생 횟수 | 누적 카운트 | 성공/실패 추적, 비즈니스 이벤트 |
| **Gauge** | 현재 상태 스냅샷 | 현재 값 | 리소스 사용량, 큐 크기, 연결 수 |

## 조합 패턴

### 1. Timer + Counter (가장 흔한 패턴)

#### 왜 함께 사용하는가?

Timer는 실행 시간과 전체 호출 횟수를 제공하지만, **성공/실패를 구분하지 못합니다**. 예외가 발생해도 Timer의 count는 증가하므로, 별도의 Counter로 성공/실패를 명확히 추적해야 합니다.

#### 실무 사례

```java
public Course createCourse(CourseCreateRequest request) {
    return createCourseTimer.record(() -> {
        try {
            Course savedCourse = courseRepository.save(request.toEntity());
            successCounter.increment();  // 성공 시에만 카운트
            return savedCourse;
        } catch (Exception e) {
            failureCounter.increment();  // 실패 시에만 카운트
            throw e;
        }
    });
}
```

#### 얻을 수 있는 인사이트

- **Timer 메트릭**:
  - `api.courses.creation.time.count`: 전체 호출 횟수 (성공 + 실패)
  - `api.courses.creation.time.sum`: 총 실행 시간
  - `api.courses.creation.time.mean`: 평균 실행 시간
  - `api.courses.creation.time.max`: 최대 실행 시간

- **Counter 메트릭**:
  - `api.courses.created.success`: 성공 횟수
  - `api.courses.created.failure`: 실패 횟수

- **계산 가능한 지표**:
  - 성공률: `success / (success + failure)`
  - 실패율: `failure / (success + failure)`
  - 평균 성공 시간: Timer의 sum을 success로 나눔

### 2. Timer + Gauge

#### 왜 함께 사용하는가?

Timer는 **작업의 실행 시간**을 측정하고, Gauge는 **현재 상태**를 보여줍니다. 예를 들어:
- Timer: API 호출이 얼마나 빠른가?
- Gauge: 현재 대기 중인 요청이 몇 개인가?

#### 실무 사례

```java
// Configuration
@Bean
public Gauge activeConnectionsGauge(MeterRegistry registry, 
                                    ConnectionPool connectionPool) {
    return Gauge.builder("db.connections.active", 
                        connectionPool, 
                        ConnectionPool::getActiveConnections)
            .register(registry);
}

// Service
public Data fetchData() {
    return queryTimer.record(() -> {
        return database.query();
    });
}
```

#### 얻을 수 있는 인사이트

- 쿼리 실행 시간이 급증 + 활성 연결 수 증가
  → 데이터베이스 병목 현상

- 쿼리 실행 시간 정상 + 활성 연결 수 최대치
  → 연결 풀 크기 부족

### 3. Timer + Counter + Gauge (통합 모니터링)

#### 프로젝트의 실제 구현

```java
// Configuration
@Configuration
public class CourseTrackerMetricsConfiguration {
    
    @Bean
    public Timer createCoursesTimer(MeterRegistry meterRegistry) {
        return Timer.builder("api.courses.creation.time")
                .description("Course creation execution time")
                .tags("operation", "create")
                .register(meterRegistry);
    }

    @Bean
    public Counter successCounter(MeterRegistry meterRegistry) {
        return Counter.builder("api.courses.created.success")
                .description("Number of successfully created courses")
                .tags("status", "success")
                .register(meterRegistry);
    }

    @Bean
    public Counter failureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("api.courses.created.failure")
                .description("Number of failed course creation attempts")
                .tags("status", "failure")
                .register(meterRegistry);
    }

    @Bean
    public Gauge createCoursesGauge(MeterRegistry meterRegistry, 
                                   CourseService courseService) {
        return Gauge.builder("api.courses.total.count", 
                            courseService, 
                            CourseService::count)
                .description("Current total number of courses")
                .register(meterRegistry);
    }
}
```

## 실무 시나리오 분석

### 시나리오 1: 정상 동작

```
Timer:           평균 200ms, count 10,000
Success Counter: 9,500
Failure Counter: 500
Gauge:          10,000개

분석:
✅ 성공률 95% (9,500 / 10,000)
✅ 응답 시간 정상
✅ 모든 성공 건이 DB에 저장됨
```

### 시나리오 2: 성능 저하 감지

```
Timer:           평균 5,000ms (급증 🔴), count 1,000
Success Counter: 100
Failure Counter: 900
Gauge:          10,000개 (변화 없음)

분석:
🔴 응답 시간 25배 증가
🔴 실패율 90%
🔴 총 개수 변화 없음

원인 추정:
- 데이터베이스 성능 문제
- 네트워크 지연
- 외부 서비스 의존성 문제
```

### 시나리오 3: 데이터 불일치 감지

```
Timer:           평균 200ms (정상), count 1,000
Success Counter: 1,000
Failure Counter: 0
Gauge:          500개만 증가 🔴

분석:
✅ 응답 시간 정상
✅ 실패 없음
🔴 성공 카운트와 실제 DB 증가량 불일치

원인 추정:
- 트랜잭션 롤백 발생
- 비동기 삭제 프로세스 동작
- 중복 데이터 자동 제거 로직
- DB 제약 조건 위반 (Silent Failure)
```

### 시나리오 4: 급격한 트래픽 증가

```
Timer:           평균 300ms (소폭 증가), count 100,000 (10배 증가 🔴)
Success Counter: 95,000
Failure Counter: 5,000
Gauge:          105,000개

분석:
✅ 성공률 95% (유지)
⚠️ 응답 시간 50% 증가 (부하 징후)
🔴 트래픽 10배 증가

조치 필요:
- 스케일 아웃 고려
- 캐싱 전략 검토
- 비동기 처리 도입
```

## 모범 사례 (Best Practices)

### 1. 일관된 네이밍 컨벤션

```java
// ✅ 좋은 예
"api.courses.creation.time"       // Timer
"api.courses.created.success"     // Counter
"api.courses.created.failure"     // Counter
"api.courses.total.count"         // Gauge

// ❌ 나쁜 예
"courseCreateTime"                // 일관성 없음
"success"                         // 너무 일반적
"coursesGauge"                    // 측정 대상 불명확
```

### 2. 의미 있는 태그 사용

```java
// ✅ 좋은 예
Timer.builder("api.request.time")
    .tags("operation", "create", 
          "resource", "course",
          "version", "v1")
    .register(registry);

// Prometheus 쿼리 예시
// api_request_time_seconds_sum{operation="create",resource="course"}
```

### 3. 비즈니스 메트릭 추가

```java
// 기술적 메트릭
Counter.builder("api.courses.created.success")

// 비즈니스 메트릭
Counter.builder("business.revenue.course.sales")
    .tags("price_tier", "premium")
    
Counter.builder("business.user.engagement.course.enrollments")
    .tags("category", "programming")
```

### 4. SLA 기반 알람 설정

```java
// Grafana 알람 예시
// 성공률이 95% 미만으로 떨어지면 알림
(api_courses_created_success_total / 
 (api_courses_created_success_total + api_courses_created_failure_total)) < 0.95

// P95 응답 시간이 1초를 초과하면 알림
histogram_quantile(0.95, api_courses_creation_time_seconds_bucket) > 1.0

// 총 코스 수가 급감하면 알림 (데이터 손실 의심)
rate(api_courses_total_count[5m]) < -10
```

## 성능 고려사항

### 메트릭 오버헤드

각 메트릭 타입의 성능 영향:

| 메트릭 | 오버헤드 | 메모리 사용 | 권장 사용 빈도 |
|-------|---------|------------|--------------|
| Counter | 매우 낮음 | 낮음 | 제한 없음 |
| Gauge | 낮음 | 낮음 | 제한 없음 |
| Timer | 중간 | 중간 (히스토그램) | 모든 API 호출 |

### Timer 최적화

```java
// ❌ 피해야 할 패턴: 너무 세밀한 측정
public void process() {
    timer1.record(() -> step1());
    timer2.record(() -> step2());
    timer3.record(() -> step3());
    // 오버헤드 증가
}

// ✅ 권장 패턴: 의미 있는 단위로 측정
public void process() {
    overallTimer.record(() -> {
        step1();
        step2();
        step3();
    });
}
```

## 결론

**Timer, Counter, Gauge는 상호 보완적**입니다:

- **Timer**: "얼마나 빠른가?" (성능)
- **Counter**: "얼마나 자주 발생하는가?" (빈도, 성공/실패)
- **Gauge**: "현재 상태는 어떤가?" (스냅샷)

실무에서는 이들을 조합하여:
1. 성능 문제 조기 감지
2. 성공률 및 SLA 모니터링
3. 데이터 무결성 검증
4. 용량 계획 수립

이러한 이유로 **책의 예시처럼 분리된 메서드가 아닌, 하나의 메서드에서 여러 메트릭을 조합**하는 것이 실무 패턴입니다.

