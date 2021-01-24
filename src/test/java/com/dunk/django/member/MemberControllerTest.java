package com.dunk.django.member;

import com.dunk.django.domain.Member;
import com.dunk.django.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class MemberControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired MemberRepository memberRepository;

    @Test
    void testAuthenticationKey() throws Exception {
        Member member = Member.builder()
                .nickname("nick")
                .email("o@o.com")
                .role(Role.USER)
                .password("asdf1234")
                .build();

        String authKey = UUID.randomUUID().toString();

        member.setAuthenticationKey(authKey);

        memberRepository.save(member);

        mockMvc.perform(get("/member/auth/"+member.getAuthenticationKey()))
                .andExpect(status().is3xxRedirection());

        Member findMember = memberRepository.findAll().get(0);

        assertEquals(findMember.getRole(), Role.USER);
    }

}
