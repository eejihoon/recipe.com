package com.dunk.django.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DjangoMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "mno")
    private Long userId;

    private String id;

    private String password;
    private String name;

    private String token; // 변경

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "DjangoMember")
    private Set<MemberRole> roleSets;

    public void addRole(MemberRole role) {
        if (roleSets == null) {
            roleSets = new HashSet<>();
        }
        roleSets.add(role);
    }

}