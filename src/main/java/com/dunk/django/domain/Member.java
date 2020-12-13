package com.dunk.django.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String token; // 변경

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String account, String password, String name, Role role) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public String getRoleKey() {
        return getRole().getKey();
    }
}