package com.dunk.django.repository;

import java.util.stream.IntStream;

import com.dunk.django.domain.DjangoMember;
import com.dunk.django.domain.MemberRole;

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

    @Test
    public void testInsert() {

        IntStream.rangeClosed(1, 10).forEach(i->{

            String password = pwEncoder.encode("1234");

            DjangoMember entity = DjangoMember.builder()
            .id("m"+i)
            .password(password)
            .name("name"+i)
            .build();

            MemberRole role = MemberRole.builder()
                .roleName("ROLE_MEMBER").build();

            entity.addRole(role);

            if(i>=8) {
                entity.addRole(MemberRole.builder().roleName("ROLE_ADMIN").build());
            }

            repository.save(entity);

        });
    }

    @Test
    public void testFindByUserName() {
        System.out.println(repository.findById("coco"));
    }


    /* @Query테스트
    쿼리 메소드로 만든 쿼리가 role까지 select하길래
    멤버 테이블만 select하려고 만들어주려고 했으나... 실패
  
    @Test
    public void testGet(){
        System.out.println(repository.getId("coco"));
    }
    */


}