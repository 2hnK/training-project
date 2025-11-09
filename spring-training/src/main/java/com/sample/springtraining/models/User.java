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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "USERS")
@Comment("사용자 정보 테이블")
public class User extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Comment("사용자 고유 ID")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Column(name = "first_name")
    @Comment("이름")
    private String firstName;

    @Column(name = "last_name")
    @Comment("성")
    private String lastName;

    @NotBlank
    @Column(name = "login_id", nullable = false)
    @Comment("계정 아이디")
    private String loginId;

    @NotBlank
    @Column(name = "password", nullable = false)
    @Comment("계정 비밀번호")
    private String password;

    @NotBlank
    @Column(name = "nickname", nullable = false)
    @Comment("닉네임")
    private String nickname;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false)
    @Comment("이메일 주소")
    private String email;

    @Comment("이메일/계정 인증 여부")
    private boolean verified;

    @Comment("잠금 여부")
    private boolean locked;

    @Column(name = "ACC_CRED_EXPIRED")
    @Comment("계정 자격 증명 만료 여부")
    private boolean accountCredentialsExpired;

    @Builder
    public User(String loginId, String password, String nickname, String email) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

}
