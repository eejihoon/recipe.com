package com.recipe.member.service;

import com.recipe.member.domain.Member;
import com.recipe.member.utils.MemberAdapter;
import com.recipe.member.dto.ChangePasswordRequest;
import com.recipe.member.dto.SignupRequest;
import com.recipe.member.repository.MemberRepository;
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

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMailService memberMailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member loginMember =
                memberRepository.findByEmail(email)
                        .orElseThrow(()->new UsernameNotFoundException("존재하지 않는 계정입니다."));

        checkDisableMember(loginMember);

        return new MemberAdapter(loginMember);
    }

    /*  signup process
    *   1.  SignupRequest to Member
    *   2.  send mail
    *   3.  member save
    *   4.  login
    *   5.  return nickname
    * */
    public String signup(SignupRequest signupRequest) {
        Member member = signupRequest.toEntity();

        memberMailService.sendMail(member);

        login(member);

        return member.getNickname();
    }

    private void checkDisableMember(Member member) {
        if (member.isDisable()) {
            throw new DisabledException("탈퇴한 유저입니다.");
        }
    }

    /*
    *  회원 이메일로 보낸 토큰과, 회원 객체에 저장된 토큰이 같은지 확인한다.
    * */
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
