package com.dunk.django.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class MemberApiController {
    private final MemberService memberService;
    private final SignupRequestValidator signupRequestValidator;

    @InitBinder("signupRequestDto")
    public void signupInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signupRequestValidator);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signupSubmit(@RequestBody @Valid SignupRequestDto signupRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return failResponse();
        }

        String name = memberService.signup(signupRequestDto);

        return successResponse(name);
    }

    private ResponseEntity successResponse(String body) {
        return ResponseEntity
                .ok(body);
    }

    private ResponseEntity failResponse() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
