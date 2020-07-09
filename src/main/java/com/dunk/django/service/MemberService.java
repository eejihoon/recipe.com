package com.dunk.django.service;

import java.util.Optional;

import com.dunk.django.domain.DjangoMember;

public interface MemberService {
    
    void register(DjangoMember member);

    Optional<DjangoMember> getMno(String id);
}