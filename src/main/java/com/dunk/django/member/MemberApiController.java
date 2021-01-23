package com.dunk.django.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberApiController {
    private final MemberService memberService;
    private final SignupRequestValidator signupRequestValidator;

    @InitBinder("signupRequest")
    public void signupInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signupRequestValidator);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signupSubmit(@RequestBody @Valid SignupRequest signupRequest, Errors errors) {
        if (errors.hasErrors()) {
            log.info(errors.getFieldError().getField());
            return new ResponseEntity<>(errors.getFieldError().getField(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String nickname = memberService.signup(signupRequest);

        return new ResponseEntity<>(nickname, HttpStatus.OK);
    }
}
