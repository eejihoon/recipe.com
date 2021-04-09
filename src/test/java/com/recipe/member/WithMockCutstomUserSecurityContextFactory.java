package com.recipe.member;

import com.recipe.member.dto.SignupRequest;
import com.recipe.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCutstomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Autowired
    private MemberService memberService;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        SignupRequest signupRequest = SignupRequest.builder()
                .email(withMockCustomUser.email())
                .nickname(withMockCustomUser.nickname())
                .password("12345678")
                .confirmPassword("12345678")
                .build();

        memberService.signup(signupRequest);

        UserDetails principal =
                memberService.loadUserByUsername(withMockCustomUser.email());
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        return context;
    }
}
