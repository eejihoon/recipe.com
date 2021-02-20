package com.recipe.member;

import com.recipe.domain.Member;
import com.recipe.domain.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class MemberApiControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;

    final String TEST_EMAIL = "cocodori@naver.com";
    final String TEST_PASSWORD = "asdf1234";
    final String TEST_NICKNAME = "kimong";

    private final String ROOT = "/";
    private final String API = "api";
    private final String MEMBER = "member";

    @AfterEach
    void removeAll() {
        memberRepository.deleteAll();
    }

    @DisplayName("회원 탈퇴")
    @WithMockCutstomUser
    @Test
    void testDisableMember() throws Exception {
        Member member = memberRepository.findAll().get(0);

        mockMvc.perform(delete(ROOT + API + ROOT + MEMBER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("12345678"))
                .andExpect(status().isOk());

        Member disabledMember = memberRepository.findById(member.getId()).get();
        assertTrue(disabledMember.isDisable());
    }

    @DisplayName("비밀번호 변경 처리")
    @WithMockCutstomUser
    @Test
    void testChangePassword() throws Exception {
        Member member = memberRepository.findByEmail("test@email.com").orElseThrow();

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setPassword("aaaabbbb");
        changePasswordRequest.setConfirmPassword("aaaabbbb");

        mockMvc.perform(put("/api/member")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk());

        Member changedMember = memberRepository.findById(member.getId()).orElseThrow();

        assertNotEquals(member.getPassword(), changedMember.getPassword());
    }

    @DisplayName("비밀번호 변경 처리 - 비밀번호 다를 경우")
    @WithMockCutstomUser
    @Test
    void testChangePasswordfailure() throws Exception {
        Member member = memberRepository.findByEmail("test@email.com").orElseThrow();

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setPassword("aaa11123");
        changePasswordRequest.setConfirmPassword("asdf1234");

        mockMvc.perform(put("/api/member")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isInternalServerError());

        Member changedMember = memberRepository.findById(member.getId()).orElseThrow();

        assertEquals(member.getPassword(), changedMember.getPassword());
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

    @DisplayName("인증 메일 재전송 테스트")
    @WithMockCutstomUser
    @Test
    void testResendMail() throws Exception {

        Member member = memberRepository.findByEmail("test@email.com").orElseThrow();

        String beforeAuthKey = member.getAuthenticationKey();

        mockMvc.perform(put(ROOT+API+"/re-sendmail"))
                .andExpect(status().isOk());

        Member member2 = memberRepository.findById(member.getId()).orElseThrow();

        String resendedAuthKey = member2.getAuthenticationKey();

        assertNotEquals(beforeAuthKey, resendedAuthKey);
    }

    @DisplayName("비밀번호 분실 메일 전송")
    @Test
    void testWithoutPasswordLoginSendMail() throws Exception {
        Member member = memberRepository.save(Member.builder()
                .email("test@test.com")
                .password("12345678")
                .role(Role.USER)
                .nickname("testNick")
                .build());

        assertNull(member.getCertification());

        mockMvc.perform(put(ROOT+API+"/withoutPasswordLogin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(member.getEmail()))
                .andExpect(status().isOk());

        Member afterMember = memberRepository.findById(member.getId()).orElseThrow();

        assertNotNull(afterMember.getCertification());
        assertNotEquals(afterMember.getCertification(), member.getCertification());
    }


    private SignupRequest getSignupRequestDto(String account, String password, String confirmPassword) {
        return SignupRequest.builder()
                .email(account)
                .password(password)
                .confirmPassword(confirmPassword)
                .nickname(TEST_NICKNAME)
                .build();
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

    private ResultActions signupRequest(SignupRequest signupRequest, ResultMatcher status) throws Exception{
        return mockMvc.perform(post(ROOT+API+ROOT+MEMBER)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status);
    }
}