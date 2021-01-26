package com.dunk.django.member;

import com.dunk.django.config.AppProperty;
import com.dunk.django.domain.Member;
import com.dunk.django.domain.Role;
import com.dunk.django.mail.EmailMessage;
import com.dunk.django.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AppProperty appProperty;
    private final TemplateEngine templateEngine;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member loginMember =
                memberRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 계정입니다."));

        return new MemberAdapter(loginMember);
    }

    /*  회원가입 절차
    *   1.  Member 객체로 초기화
    *   2.  이메일 전송
    *   3.  save
    *   4.  로그인
    *   5.  닉네임 반환
    * */
    public String signup(SignupRequest signupRequest) {
        Member member = Member.builder()
                .email(signupRequest.email)
                .nickname(signupRequest.getNickname())
                .password(encoded(signupRequest.getPassword()))
                .authenticationKey(UUID.randomUUID().toString())
                .role(Role.USER)
                .build();

        sendMail(member);

        Member newMember = memberRepository.save(member);

        login(newMember);

        return newMember.getNickname();
    }

    public void sendMail(Member member) {
        Context context = new Context();
        String authKey = UUID.randomUUID().toString();

        member.setAuthenticationKey(authKey);

        context.setVariable("link", "/member/auth/"+member.getAuthenticationKey());
        context.setVariable("username", member.getNickname());
        context.setVariable("link-description", "이메일 인증");
        context.setVariable("host", appProperty.getHost());

        String message = templateEngine.process("mail/authentication-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(member.getEmail())
                .subject("레시피 회원가입 인증 메일입니다.")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }

    private void login(Member loginMember) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new MemberAdapter(loginMember),
                loginMember.getPassword(),
                List.of(new SimpleGrantedAuthority(loginMember.getRoleKey()))
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    public boolean checkAuthenticationKey(String authenticationKey, Member member) {
        boolean result = member.checkKey(authenticationKey);

        memberRepository.save(member);

        return result;
    }

    private String encoded(String password) {
        return passwordEncoder.encode(password);
    }
}
