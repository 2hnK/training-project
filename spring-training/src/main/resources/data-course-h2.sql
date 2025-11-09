INSERT INTO COURSES (name, category, rating, description, created_at, updated_at) VALUES
('Rapid Spring Boot Application Development', 'Spring', 4, 'Spring Boot gives all the power of the Spring Framework without all of the complexities', '2025-08-05 10:30:00', '2025-09-12 14:20:00'),
('Mastering Spring Security', 'Spring', 5, 'Learn how to secure your Spring applications with authentication, authorization, and OAuth2 integration', '2025-08-15 09:15:00', '2025-09-25 16:45:00'),
('Spring Data JPA Fundamentals', 'Database', 4, 'Master database operations with Spring Data JPA, repositories, and query methods for efficient data access', '2025-08-22 11:00:00', '2025-10-01 10:30:00'),
('Spring MVC Web Development', 'Spring', 3, 'Build dynamic web applications using Spring MVC, Thymeleaf templates, and form handling techniques', '2025-08-10 14:20:00', '2025-08-28 09:10:00'),
('Spring Cloud Microservices', 'Microservices', 5, 'Develop scalable microservices architecture with Spring Cloud, service discovery, and distributed systems', '2025-09-01 08:45:00', '2025-10-05 15:30:00'),
('Reactive Spring WebFlux', 'Spring', 5, 'Build reactive APIs with Spring WebFlux, Project Reactor, and backpressure support', '2025-09-10 13:10:00', '2025-10-08 11:20:00'),
('Spring Boot Actuator & Monitoring', 'DevOps', 4, 'Monitor and manage apps with Actuator endpoints, Micrometer metrics, and health checks', '2025-08-18 10:00:00', '2025-09-18 14:50:00'),
('Spring Batch Processing', 'Spring', 3, 'Create robust ETL jobs using Spring Batch readers, processors, writers, and scheduling', '2025-08-25 15:30:00', '2025-09-20 12:15:00'),
('Testing Spring Applications', 'Testing', 4, 'Write unit, slice, and integration tests with JUnit 5, Mockito, and Testcontainers', '2025-09-05 09:40:00', '2025-10-03 13:25:00');

-- Chapters 테이블 초기 데이터
-- Course 1: Rapid Spring Boot Application Development (id = 1)
INSERT INTO CHAPTERS (COURSE_ID, TITLE, CONTENT, ORDER_INDEX, CREATED_AT, UPDATED_AT) VALUES
(1, 'Introduction to Spring Boot', 'Learn the basics of Spring Boot, its advantages, and why it has become the de facto framework for Java web applications.', 1, '2025-08-05 11:00:00', '2025-08-05 11:00:00'),
(1, 'Setting Up Your First Spring Boot Project', 'Step-by-step guide to creating a Spring Boot project using Spring Initializr and understanding the project structure.', 2, '2025-08-05 12:00:00', '2025-08-05 12:00:00'),
(1, 'Understanding Auto-Configuration', 'Deep dive into Spring Boot auto-configuration mechanism and how to customize it for your needs.', 3, '2025-08-05 13:00:00', '2025-08-05 13:00:00'),
(1, 'Building RESTful APIs', 'Create robust REST APIs using Spring Boot REST controllers, proper HTTP methods, and status codes.', 4, '2025-08-05 14:00:00', '2025-08-05 14:00:00'),
(1, 'Application Properties and Profiles', 'Master configuration management using application.properties, YAML files, and Spring profiles for different environments.', 5, '2025-08-05 15:00:00', '2025-08-05 15:00:00');

-- Course 2: Mastering Spring Security (id = 2)
INSERT INTO CHAPTERS (COURSE_ID, TITLE, CONTENT, ORDER_INDEX, CREATED_AT, UPDATED_AT) VALUES
(2, 'Spring Security Fundamentals', 'Introduction to Spring Security architecture, authentication vs authorization, and security filter chain.', 1, '2025-08-15 10:00:00', '2025-08-15 10:00:00'),
(2, 'User Authentication Strategies', 'Implement various authentication methods including form login, HTTP Basic, and JWT-based authentication.', 2, '2025-08-15 11:00:00', '2025-08-15 11:00:00'),
(2, 'Role-Based Access Control', 'Configure method and URL-based security with roles and authorities for fine-grained access control.', 3, '2025-08-15 12:00:00', '2025-08-15 12:00:00'),
(2, 'OAuth2 and OpenID Connect', 'Integrate OAuth2 providers like Google, GitHub, and implement social login in your applications.', 4, '2025-08-15 13:00:00', '2025-08-15 13:00:00'),
(2, 'Security Best Practices', 'Learn about CSRF protection, password encoding, session management, and common security vulnerabilities.', 5, '2025-08-15 14:00:00', '2025-08-15 14:00:00');

-- Course 3: Spring Data JPA Fundamentals (id = 3)
INSERT INTO CHAPTERS (COURSE_ID, TITLE, CONTENT, ORDER_INDEX, CREATED_AT, UPDATED_AT) VALUES
(3, 'JPA and Hibernate Basics', 'Understanding JPA specification, Hibernate ORM, and entity lifecycle in Spring Data JPA.', 1, '2025-08-22 11:30:00', '2025-08-22 11:30:00'),
(3, 'Entity Relationships', 'Master OneToMany, ManyToOne, ManyToMany relationships, cascade operations, and fetch strategies.', 2, '2025-08-22 12:30:00', '2025-08-22 12:30:00'),
(3, 'Spring Data Repositories', 'Explore JpaRepository, custom query methods, named queries, and repository interface patterns.', 3, '2025-08-22 13:30:00', '2025-08-22 13:30:00'),
(3, 'JPQL and Native Queries', 'Write complex queries using JPQL, Criteria API, and native SQL for advanced data operations.', 4, '2025-08-22 14:30:00', '2025-08-22 14:30:00'),
(3, 'Performance Optimization', 'Learn about N+1 problem, query optimization, caching strategies, and database indexing.', 5, '2025-08-22 15:30:00', '2025-08-22 15:30:00');

-- Course 4: Spring MVC Web Development (id = 4)
INSERT INTO CHAPTERS (COURSE_ID, TITLE, CONTENT, ORDER_INDEX, CREATED_AT, UPDATED_AT) VALUES
(4, 'Spring MVC Architecture', 'Understanding the MVC pattern, DispatcherServlet, and request processing lifecycle in Spring.', 1, '2025-08-10 15:00:00', '2025-08-10 15:00:00'),
(4, 'Controllers and Request Mapping', 'Create controllers, handle HTTP requests, and map URLs using various annotation strategies.', 2, '2025-08-10 16:00:00', '2025-08-10 16:00:00'),
(4, 'Thymeleaf Template Engine', 'Build dynamic web pages using Thymeleaf syntax, expressions, and template inheritance.', 3, '2025-08-10 17:00:00', '2025-08-10 17:00:00'),
(4, 'Form Handling and Validation', 'Implement form submission, data binding, and validation using Bean Validation API.', 4, '2025-08-10 18:00:00', '2025-08-10 18:00:00');

-- Course 5: Spring Cloud Microservices (id = 5)
INSERT INTO CHAPTERS (COURSE_ID, TITLE, CONTENT, ORDER_INDEX, CREATED_AT, UPDATED_AT) VALUES
(5, 'Microservices Architecture Overview', 'Learn microservices principles, benefits, challenges, and when to use this architectural style.', 1, '2025-09-01 09:00:00', '2025-09-01 09:00:00'),
(5, 'Service Discovery with Eureka', 'Implement service registration and discovery using Netflix Eureka for dynamic service location.', 2, '2025-09-01 10:00:00', '2025-09-01 10:00:00'),
(5, 'API Gateway Pattern', 'Build a unified entry point using Spring Cloud Gateway for routing, filtering, and load balancing.', 3, '2025-09-01 11:00:00', '2025-09-01 11:00:00'),
(5, 'Distributed Configuration', 'Centralize configuration management using Spring Cloud Config Server for all microservices.', 4, '2025-09-01 12:00:00', '2025-09-01 12:00:00'),
(5, 'Circuit Breaker and Resilience', 'Implement fault tolerance using Resilience4j circuit breaker, retry, and fallback patterns.', 5, '2025-09-01 13:00:00', '2025-09-01 13:00:00'),
(5, 'Distributed Tracing', 'Monitor microservices using Spring Cloud Sleuth and Zipkin for request tracing across services.', 6, '2025-09-01 14:00:00', '2025-09-01 14:00:00');

-- Course 6: Reactive Spring WebFlux (id = 6)
INSERT INTO CHAPTERS (COURSE_ID, TITLE, CONTENT, ORDER_INDEX, CREATED_AT, UPDATED_AT) VALUES
(6, 'Reactive Programming Concepts', 'Introduction to reactive streams, backpressure, and the reactive manifesto principles.', 1, '2025-09-10 14:00:00', '2025-09-10 14:00:00'),
(6, 'Project Reactor Basics', 'Master Mono and Flux types, operators, and reactive stream transformations in Project Reactor.', 2, '2025-09-10 15:00:00', '2025-09-10 15:00:00'),
(6, 'WebFlux Controllers', 'Build reactive REST APIs using annotated controllers and functional routing in Spring WebFlux.', 3, '2025-09-10 16:00:00', '2025-09-10 16:00:00'),
(6, 'Reactive Data Access', 'Connect to databases using R2DBC for reactive, non-blocking database operations.', 4, '2025-09-10 17:00:00', '2025-09-10 17:00:00'),
(6, 'Testing Reactive Applications', 'Write tests for reactive code using StepVerifier and WebTestClient for comprehensive coverage.', 5, '2025-09-10 18:00:00', '2025-09-10 18:00:00');

-- Course 7: Spring Boot Actuator & Monitoring (id = 7)
INSERT INTO CHAPTERS (COURSE_ID, TITLE, CONTENT, ORDER_INDEX, CREATED_AT, UPDATED_AT) VALUES
(7, 'Actuator Endpoints Overview', 'Explore built-in actuator endpoints for health, metrics, info, and application management.', 1, '2025-08-18 11:00:00', '2025-08-18 11:00:00'),
(7, 'Custom Health Indicators', 'Create custom health checks to monitor external dependencies and application-specific health.', 2, '2025-08-18 12:00:00', '2025-08-18 12:00:00'),
(7, 'Metrics with Micrometer', 'Implement custom metrics, counters, gauges, and timers using Micrometer framework.', 3, '2025-08-18 13:00:00', '2025-08-18 13:00:00'),
(7, 'Integrating Prometheus and Grafana', 'Set up monitoring dashboards by integrating Prometheus for metrics collection and Grafana for visualization.', 4, '2025-08-18 14:00:00', '2025-08-18 14:00:00');

-- Course 8: Spring Batch Processing (id = 8)
INSERT INTO CHAPTERS (COURSE_ID, TITLE, CONTENT, ORDER_INDEX, CREATED_AT, UPDATED_AT) VALUES
(8, 'Batch Processing Fundamentals', 'Understanding batch processing concepts, job, step, and chunk-oriented processing in Spring Batch.', 1, '2025-08-25 16:00:00', '2025-08-25 16:00:00'),
(8, 'Readers, Processors, and Writers', 'Configure ItemReaders to read data, ItemProcessors for transformation, and ItemWriters for output.', 2, '2025-08-25 17:00:00', '2025-08-25 17:00:00'),
(8, 'Job Configuration and Scheduling', 'Define job flows, decision steps, and schedule batch jobs using Spring Scheduler or Quartz.', 3, '2025-08-25 18:00:00', '2025-08-25 18:00:00'),
(8, 'Error Handling and Retry', 'Implement skip policies, retry logic, and failure handling for robust batch processing.', 4, '2025-08-25 19:00:00', '2025-08-25 19:00:00');

-- Course 9: Testing Spring Applications (id = 9)
INSERT INTO CHAPTERS (COURSE_ID, TITLE, CONTENT, ORDER_INDEX, CREATED_AT, UPDATED_AT) VALUES
(9, 'Testing Fundamentals with JUnit 5', 'Learn JUnit 5 basics, assertions, test lifecycle, and parameterized tests for Spring applications.', 1, '2025-09-05 10:00:00', '2025-09-05 10:00:00'),
(9, 'Mocking with Mockito', 'Master Mockito framework for creating mocks, stubs, and verifying interactions in unit tests.', 2, '2025-09-05 11:00:00', '2025-09-05 11:00:00'),
(9, 'Spring Boot Test Slices', 'Use @WebMvcTest, @DataJpaTest, and other slice annotations for focused integration testing.', 3, '2025-09-05 12:00:00', '2025-09-05 12:00:00'),
(9, 'Integration Testing with TestContainers', 'Set up real databases and external services using Testcontainers for realistic integration tests.', 4, '2025-09-05 13:00:00', '2025-09-05 13:00:00'),
(9, 'Test Coverage and Best Practices', 'Achieve comprehensive test coverage using JaCoCo, and follow testing best practices and patterns.', 5, '2025-09-05 14:00:00', '2025-09-05 14:00:00');