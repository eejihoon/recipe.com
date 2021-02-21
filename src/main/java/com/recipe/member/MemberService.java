package com.recipe.member;

import com.recipe.config.AppProperty;
import com.recipe.domain.Member;
import com.recipe.domain.Role;
import com.recipe.mail.EmailMessage;
import com.recipe.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
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
import java.util.Optional;
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

        checkDisableMember(loginMember);

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

    public void loginSendMail(String email) throws UsernameNotFoundException, DisabledException{
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        checkDisableMember(member);

        member.setCertificationNumber();

        Context context = getContext(member,
                "/member/withoutPasswordLogin?certification="+member.getCertification()+"&email="+member.getEmail());
        String message = templateEngine.process("mail/withoutPasswordLogin", context);
        EmailMessage emailMessage = getEmailMessage(member, message);

        emailService.sendEmail(emailMessage);
    }

    private void checkDisableMember(Member member) {
        if (member.isDisable()) {
            throw new DisabledException("탈퇴한 유저입니다.");
        }
    }

    public void sendMail(Member member) {
        String authKey = UUID.randomUUID().toString();
        member.setAuthenticationKey(authKey);
        memberRepository.save(member);

        Context context = getContext(member, "/member/auth/"+ member.getAuthenticationKey());

        String message = templateEngine.process("mail/authentication-link", context);

        EmailMessage emailMessage = getEmailMessage(member, message);

        emailService.sendEmail(emailMessage);
    }

    public boolean checkAuthenticationKey(String authenticationKey, Member member) {
        boolean result = member.checkKey(authenticationKey);

        memberRepository.save(member);

        return result;
    }

    public void withoutPasswordLogin(String certification, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        if(isEquals(certification, member)) {
            login(member);

        }
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest, Member member) {
        member.changePassword(encoded(changePasswordRequest.getPassword()));
        memberRepository.save(member);
    }

    public boolean disableMember(Member member, String password) {
        if (matches(member, password)) {
            member.setDisable();
            memberRepository.save(member);
            return true;
        }
        return false;
    }

    private EmailMessage getEmailMessage(Member member, String message) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(member.getEmail())
                .subject("레시피북 인증 메일입니다.")
                .message(message)
                .build();
        return emailMessage;
    }

    private Context getContext(Member member, String path) {
        Context context = new Context();

        context.setVariable("link", path);
        context.setVariable("username", member.getNickname());
        context.setVariable("link-description", "이메일 인증");
        context.setVariable("host", appProperty.getHost());

        return context;
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

    private String encoded(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean isEquals(String certification, Member member) {
        return member.getCertification().equals(certification);
    }

    private boolean matches(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }
}
