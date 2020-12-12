package com.dunk.django.member;

import java.util.Optional;

import com.dunk.django.domain.DjangoMember;

public interface MemberService {
    
    void register(DjangoMember member);

    Optional<DjangoMember> getMno(String id);
}