package com.recipe.member.controller;

import com.recipe.config.security.LoginMember;
import com.recipe.member.domain.Member;
import com.recipe.member.utils.MemberAdapter;
import com.recipe.member.service.MemberService;
import com.recipe.member.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        SignupRequest signupRequest = new SignupRequest();
        model.addAttribute("signupForm", signupRequest);

        return "member/signup";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    @GetMapping("/setting")
    public String settingForm() {
        return "member/setting/setting";
    }

    @GetMapping("/member/auth/{key}")
    public String authenticationKeyCheck(@PathVariable String key,
                                         @LoginMember Member loginMember,
                                         RedirectAttributes redirect) {
        if (Objects.nonNull(loginMember)) {
            boolean result = memberService.checkAuthenticationKey(key, loginMember);
            if (result == false) {
                redirect.addFlashAttribute("error", "잘못된 인증입니다.");
            } else {
                redirect.addFlashAttribute("verified", "인증되었습니다. 이제 레시피를 등록할 수 있습니다.");
            }
        }

        return "redirect:/";
    }

    @GetMapping("/withoutPasswordLogin")
    public String withoutpasswordLoginForm() {
        return "member/withoutPasswordLogin";
    }

    @GetMapping("/member/withoutPasswordLogin")
    public String withoutPasswordLogin(String certification, String email) {
        log.info("certification : {}", certification);
        log.info("email : {} ", email);

        memberService.withoutPasswordLogin(certification, email);

        /*
        * TODO 비밀번호 변경 페이지로 redirect 하도록 변경
        * */
        return "redirect:/";
    }

    @GetMapping("/setting/password")
    public String changePassword() {
        return "member/setting/password";
    }
}