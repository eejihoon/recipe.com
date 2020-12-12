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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @AfterEach
    void removeAll() {
        memberRepository.deleteAll();
    }

    private SignupRequestDto signupRequestDto =
            SignupRequestDto.builder()
                    .account("testAccount")
                    .password("testPassword")
                    .confirmPassword("testPassword")
                    .name("kimong")
                    .build();

    @DisplayName("회원가입 처리 - 성공")
    @Test
    void signupSubmit() throws Exception {
        signupAction(signupRequestDto, status().isOk());
        List<Member> allMember = memberRepository.findAll();
        assertTrue(memberRepository.existsByAccount("testAccount"));
        assertNotNull(allMember);
        assertEquals(allMember.get(0).getName(), "kimong");
        assertEquals(allMember.get(0).getRole(), Role.USER);
    }

    @DisplayName("회원가입 실패 - 아이디 중복")
    @Test
    void signupDuplicateAccount() throws Exception {
        signupAction(signupRequestDto, status().isOk());
        signupAction(signupRequestDto, status().isInternalServerError());
    }

    @DisplayName("회원가입 실패 - 비밀번호 재입력 다를 경우")
    @Test
    void signupDifferentPassword() throws Exception {
        SignupRequestDto differentPasswordRequest =
                SignupRequestDto.builder()
                .account("asdf")
                .password("zzzz1234")
                .confirmPassword("asdf1234")
                .name("tester")
                .build();

        signupAction(differentPasswordRequest, status().is5xxServerError());
    }

    private ResultActions signupAction(SignupRequestDto signupRequestDto,ResultMatcher status) throws Exception{
        return mockMvc.perform(post("/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andExpect(status);
    }



}