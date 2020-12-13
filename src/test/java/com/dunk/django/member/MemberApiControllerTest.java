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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MemberApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    final static String TEST_ACCOUNT = "cocodori";
    final static String TEST_PASSWORD = "asdf1234";
    final static String TEST_NAME = "kimong";
    final static ResultMatcher HTTP_OK = status().isOk();

    @AfterEach
    void removeAll() {
        memberRepository.deleteAll();
    }

    @DisplayName("회원가입 처리 - 성공")
    @Test
    void signupSubmit() throws Exception {
        SignupRequestDto signupRequestDto = getSignupRequestDto(TEST_ACCOUNT, TEST_PASSWORD, TEST_PASSWORD);

        signupRequest(signupRequestDto, status().isOk())
                .andExpect(authenticated().withUsername(signupRequestDto.getAccount()));

        List<Member> allMember = memberRepository.findAll();

        assertTrue(memberRepository.existsByAccount(TEST_ACCOUNT));
        assertNotNull(allMember);
        assertEquals(allMember.get(0).getName(), TEST_NAME);
        assertEquals(allMember.get(0).getRole(), Role.USER);
    }

    @DisplayName("회원가입 실패 - 아이디 중복")
    @Test
    void signupDuplicateAccount() throws Exception {
        SignupRequestDto signupRequestDto = getSignupRequestDto(TEST_ACCOUNT, TEST_PASSWORD, TEST_PASSWORD);

        signupRequest(signupRequestDto, status().isOk());
        signupRequest(signupRequestDto, status().isInternalServerError());
    }

    @DisplayName("회원가입 실패 - 비밀번호 재입력 다를 경우")
    @Test
    void signupDifferentPassword() throws Exception {
        SignupRequestDto differentPasswordRequest =
                getSignupRequestDto(TEST_ACCOUNT, TEST_PASSWORD, "zzzz1234");

        signupRequest(differentPasswordRequest, status().is5xxServerError());
    }

    @Test
    void testLoginSubmit() throws Exception{
        SignupRequestDto signupRequestDto =
                getSignupRequestDto(TEST_ACCOUNT, TEST_PASSWORD, TEST_PASSWORD);

        signupRequest(signupRequestDto, status().isOk());

        mockMvc.perform(post("/login")
            .with(csrf())
            .param("username", TEST_ACCOUNT)
            .param("password", TEST_PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername(TEST_ACCOUNT));
    }

    private SignupRequestDto getSignupRequestDto(String account, String password, String confirmPassword) {
        return SignupRequestDto.builder()
                .account(account)
                .password(password)
                .confirmPassword(confirmPassword)
                .name(TEST_NAME)
                .build();
    }

    private ResultActions signupRequest(SignupRequestDto signupRequestDto, ResultMatcher status) throws Exception{
        return mockMvc.perform(post("/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andExpect(status);
    }




}