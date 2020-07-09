package com.dunk.django.service;

import com.dunk.django.domain.DjangoMember;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTests {

    @Autowired
    MemberCheckService service;

    @Test
    public void testRegister() {
        
        DjangoMember member = DjangoMember.builder()
        .id("serviceTest2")
        .password("1234")
        .name("catcat2")
        .build();

        service.register(member);
    }
}