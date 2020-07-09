package com.dunk.django.dto;

import java.util.Collection;

import com.google.auto.value.AutoValue.Builder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class DjangoMemberDTO extends User {

    private Long userId;

    private String name;

    public DjangoMemberDTO(Long userId,String username, String password, String name, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
 
        this.name = name;
    }
}