package com.sample.springtraining.models;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COURSES")
@Comment("강좌 정보 테이블")
@Getter
@Setter
@NoArgsConstructor
public class Course extends BaseEntity {

    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("강좌 고유 ID")
    private Long id;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @NotBlank(message = "강좌명은 필수입니다")
    @Column(name = "name", nullable = false)
    @Comment("강좌명")
    private String name;

    @NotBlank(message = "강좌 설명은 필수입니다")
    @Column(name = "description", nullable = false, length = 1000)
    @Comment("강좌 상세 설명")
    private String description;

    @NotBlank(message = "카테고리는 필수입니다")
    @Column(name = "category", nullable = false)
    @Comment("강좌 카테고리")
    private String category;

    @Min(value = 1, message = "평점은 최소 1점입니다")
    @Max(value = 5, message = "평점은 최대 5점입니다")
    @Column(name = "rating", nullable = false)
    @Comment("강좌 평점")
    private int rating;

    @Builder
    public Course(String name, String category, int rating, String description) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.rating = rating;
        this.chapters = new ArrayList<>();
    }

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
        chapter.setCourse(this);
    }

    public void removeChapter(Chapter chapter) {
        chapters.remove(chapter);
        chapter.setCourse(null);
    }
}
