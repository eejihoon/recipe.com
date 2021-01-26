package com.dunk.django.member;

import com.dunk.django.domain.Member;
import com.dunk.django.domain.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class MemberApiControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired MemberRepository memberRepository;

    final static String TEST_EMAIL = "cocodori@naver.com";
    final static String TEST_PASSWORD = "asdf1234";
    final static String TEST_NICKNAME = "kimong";
    final static ResultMatcher HTTP_OK = status().isOk();

    @AfterEach
    void removeAll() {
        memberRepository.deleteAll();
    }

    @DisplayName("회원가입 처리 - 성공")
    @Test
    void signupSubmit() throws Exception {
        SignupRequest signupRequest = getSignupRequestDto(TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);

        signupRequest(signupRequest, status().isOk())
                .andExpect(authenticated().withUsername(signupRequest.getEmail()));

        List<Member> allMember = memberRepository.findAll();

        assertTrue(memberRepository.existsByEmail(TEST_EMAIL));

        assertNotNull(allMember);
        assertEquals(allMember.get(0).getNickname(), TEST_NICKNAME);
        assertEquals(allMember.get(0).getRole(), Role.USER);
        assertEquals(allMember.get(0).isVerified(), false);
    }

    @DisplayName("회원가입 실패 - 아이디 중복")
    @Test
    void signupDuplicateAccount() throws Exception {
        SignupRequest signupRequest = getSignupRequestDto(TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);

        signupRequest(signupRequest, status().isOk());
        signupRequest(signupRequest, status().isInternalServerError());
    }

    @DisplayName("회원가입 실패 - 비밀번호 재입력 다를 경우")
    @Test
    void signupDifferentPassword() throws Exception {
        SignupRequest differentPasswordRequest =
                getSignupRequestDto(TEST_EMAIL, TEST_PASSWORD, "zzzz1234");

        signupRequest(differentPasswordRequest, status().is5xxServerError());
    }

    @DisplayName("로그인 테스트")
    @Test
    void testLoginSubmit() throws Exception{
        SignupRequest signupRequest =
                getSignupRequestDto(TEST_EMAIL, TEST_PASSWORD, TEST_PASSWORD);

        signupRequest(signupRequest, status().isOk());

        mockMvc.perform(post("/login")
            .with(csrf())
            .param("username", TEST_EMAIL)
            .param("password", TEST_PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername(TEST_EMAIL));
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
        assertTrue(verifiedMember.isVerified());
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

    private SignupRequest getSignupRequestDto(String account, String password, String confirmPassword) {
        return SignupRequest.builder()
                .email(account)
                .password(password)
                .confirmPassword(confirmPassword)
                .nickname(TEST_NICKNAME)
                .build();
    }

    private ResultActions signupRequest(SignupRequest signupRequest, ResultMatcher status) throws Exception{
        return mockMvc.perform(post("/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status);
    }
}