package com.dunk.django.repository;

import java.util.stream.IntStream;

import com.dunk.django.domain.Member;
import com.dunk.django.domain.Role;

import com.dunk.django.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MemberRepositoryTests {
    
    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder pwEncoder;

}