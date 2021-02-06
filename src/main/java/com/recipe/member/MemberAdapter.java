package com.recipe.member;

import com.recipe.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Arrays;

@Getter
public class MemberAdapter extends User implements Serializable {
    private Member member;

    public MemberAdapter(Member member) {
        super(member.getEmail(), member.getPassword(), Arrays.asList(new SimpleGrantedAuthority(member.getRoleKey())));
        this.member = member;
    }
}
