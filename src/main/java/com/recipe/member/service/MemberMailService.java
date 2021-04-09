package com.recipe.member.service;

import com.recipe.config.AppProperty;
import com.recipe.mail.EmailMessage;
import com.recipe.mail.EmailService;
import com.recipe.member.domain.Member;
import com.recipe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberMailService {
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperty appProperty;

    public void loginSendMail(String email) throws UsernameNotFoundException, DisabledException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        if (member.isDisable()) {
            throw new DisabledException("탈퇴한 회원입니다.");
        }
        member.setCertificationNumber();

        Context context = getContext(member,
                "/member/withoutPasswordLogin?certification=" + member.getCertification() + "&email=" + member.getEmail());
        String message =
                templateEngine.process("mail/withoutPasswordLogin", context);
        EmailMessage emailMessage = getEmailMessage(member, message);
        emailService.sendEmail(emailMessage);
    }

    public void sendMail(Member member) {
        String authKey = UUID.randomUUID().toString();
        member.setAuthenticationKey(authKey);
        memberRepository.save(member);

        Context context =
                getContext(member, "/member/auth/" + member.getAuthenticationKey());
        String message =
                templateEngine.process("mail/authentication-link", context);

        EmailMessage emailMessage = getEmailMessage(member, message);

        emailService.sendEmail(emailMessage);
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


}
