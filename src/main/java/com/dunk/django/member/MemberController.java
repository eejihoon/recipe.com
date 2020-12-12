package com.dunk.django.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/signup")
    public void signupForm(Model model) {
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        model.addAttribute("signupForm", signupRequestDto);
    }

    @GetMapping("/login")
    public void loginForm() {
    }
}