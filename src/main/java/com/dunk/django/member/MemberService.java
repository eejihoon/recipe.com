package com.dunk.django.member;

import com.dunk.django.domain.Member;
import com.dunk.django.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public String signup(SignupRequestDto signupRequestDto) {
        Member member = Member.builder()
                .account(signupRequestDto.account)
                .password(encoded(signupRequestDto.getPassword()))
                .name(signupRequestDto.getName())
                .role(Role.USER)
                .build();

        Member newMember = memberRepository.save(member);
        
        return newMember.getName();

    }

    private String encoded(String password) {
        return passwordEncoder.encode(password);
    }
}
