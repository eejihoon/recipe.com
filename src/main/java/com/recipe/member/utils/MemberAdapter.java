package com.recipe.member.utils;

import com.recipe.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Getter
public class MemberAdapter extends User implements Serializable {
    private Member member;

    public MemberAdapter(Member member) {
        super(member.getEmail(), member.getPassword(), Arrays.asList(new SimpleGrantedAuthority(member.getRoleKey())));
        this.member = member;
    }
}
