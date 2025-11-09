package com.sample.springtraining.models;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AUTHORITIES")
@Comment("권한 테이블")
@Getter
@Setter
@NoArgsConstructor
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_ID")
    @Comment("권한 고유 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME", nullable = false,
                foreignKey = @jakarta.persistence.ForeignKey(name = "fk_authorities_users"))
    @Comment("사용자 외래키 (username)")
    private User user;

    @Column(name = "AUTHORITY", nullable = false)
    @Comment("권한")
    private String authority;

    @Builder
    public Authority(User user, String authority) {
        this.user = user;
        this.authority = authority;
    }
}
