package com.dunk.django.repository;

import java.util.Optional;

import com.dunk.django.domain.DjangoMember;

import org.springframework.data.jpa.repository.JpaRepository;



public interface MemberRepository extends JpaRepository<DjangoMember, Long> {
    
    Optional<DjangoMember> findById(String id);

}