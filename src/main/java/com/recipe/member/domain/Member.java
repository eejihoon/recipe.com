package com.recipe.member.domain;

import com.recipe.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private boolean disable = false;

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

    //TODO CQRS 위반.
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

    //회원탈퇴 시 disable true로 변경
    public void setDisable() {
        this.disable = true;
    }

    /*
     *  가입 후 24시간 이내에 인증하지 않은 회원인지 체크
     * */
    public boolean isAuthenticationTimeOut() {
        return LocalDateTime.now()
                .isAfter(regdate.plusHours(24));
    }
}