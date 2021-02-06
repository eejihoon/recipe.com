package com.recipe.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    private String authenticationKey;

    private boolean verified;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String certification; //비밀번호 분실 시 사용

    @Builder
    public Member(String email, String password, String nickname, String authenticationKey, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authenticationKey = authenticationKey;
        this.role = role;
        this.verified = false;
    }

    public String getRoleKey() {
        return getRole().getKey();
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public boolean checkKey(String authenticationKey) {
        if (this.authenticationKey.equals(authenticationKey)) {
            role = Role.USER;
            this.verified = true;
        }

        return this.verified;
    }

    public void setCertificationNumber() {
        this.certification = UUID.randomUUID().toString();
    }

    public void changePassword(String password) {
        this.password = password;
    }
}