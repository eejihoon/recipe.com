package com.dunk.django.member;

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

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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
                .role(Role.TEMPORARY)
                .build();

        sendMail(member);

        Member newMember = memberRepository.save(member);

        login(newMember);

        return newMember.getNickname();
    }

    private void sendMail(Member member) {
        String authenticationKey = UUID.randomUUID().toString();

        EmailMessage emailMessage = EmailMessage.builder()
                .to(member.getEmail())
                .subject(member.getNickname()+" 님 가입을 축하합니다.")
                .message(authenticationKey)
                .build();

        member.setAuthenticationKey(authenticationKey);

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

    public void checkAuthenticationKey(String authenticationKey, Member member) {
        member.checkKey(authenticationKey);

        memberRepository.save(member);
    }

    private String encoded(String password) {
        return passwordEncoder.encode(password);
    }
}
