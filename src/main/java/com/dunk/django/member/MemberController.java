package com.dunk.django.member;

import com.dunk.django.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public void signupForm(Model model) {
        SignupRequest signupRequest = new SignupRequest();
        model.addAttribute("signupForm", signupRequest);
    }

    @GetMapping("/login")
    public void loginForm() {
    }

    @GetMapping("/member/auth/{key}")
    public String authenticationKeyCheck(@PathVariable String key, @AuthenticationPrincipal
            MemberAdapter memberAdapter) {
        if (Objects.nonNull(memberAdapter)) {
            memberService.checkAuthenticationKey(key, memberAdapter.getMember());
        }

        return "redirect:/";
    }
}