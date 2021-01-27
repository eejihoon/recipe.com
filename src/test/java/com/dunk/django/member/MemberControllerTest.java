package com.dunk.django.member;

import com.dunk.django.domain.Member;
import com.dunk.django.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class MemberControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("비밀번호없이 로그인 하기")
    void testWithoutPassword() throws Exception {
        Member member = memberRepository.save(Member.builder()
                .email("test@test.com")
                .password("12345678")
                .role(Role.USER)
                .nickname("testNick")
                .build());

        member.setCertificationNumber();

        mockMvc.perform(get("/member/withoutPasswordLogin")
        .queryParam("certification", member.getCertification())
        .queryParam("email", member.getEmail()))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated().withUsername(member.getEmail()));
    }

    @DisplayName("이메일 인증 테스트")
    @WithMockCutstomUser
    @Test
    void testCheckEmail() throws Exception {
        Member member = memberRepository.findAll().get(0);

        assertFalse(member.isVerified());

        mockMvc.perform(get("/member/auth/"+member.getAuthenticationKey()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("verified"));

        Member verifiedMember = memberRepository.findById(member.getId()).orElseThrow();
        Assertions.assertTrue(verifiedMember.isVerified());
    }

    @DisplayName("이메일 인증 실패 테스트")
    @WithMockCutstomUser
    @Test
    void testCheckEmailFailure() throws Exception {
        Member member = memberRepository.findAll().get(0);

        assertFalse(member.isVerified());

        mockMvc.perform(get("/member/auth/"+"wrong-auth-key"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));

        Member verifiedMember = memberRepository.findById(member.getId()).orElseThrow();
        assertFalse(verifiedMember.isVerified());
    }

    @Test
    @DisplayName("비밀번호 없이 로그인 폼")
    void testWithoutPasswordForm() throws Exception {
        mockMvc.perform(get("/withoutPasswordLogin"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 폼")
    void testSignupForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 폼")
    void testLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCutstomUser
    @DisplayName("비밀번호 변경 폼")
    void testChangePasswordForm() throws Exception {
        mockMvc.perform(get("/setting/password"))
                .andExpect(status().isOk());
    }

}
