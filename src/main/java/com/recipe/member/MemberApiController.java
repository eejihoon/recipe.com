package com.recipe.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberApiController {
    private final MemberService memberService;
    private final SignupRequestValidator signupRequestValidator;
    private final ChangePasswordRequestValidator changePasswordRequestValidator;

    @InitBinder("signupRequest")
    public void signupInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signupRequestValidator);
    }

    @InitBinder("changePasswordRequest")
    public void changePasswordInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(changePasswordRequestValidator);
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

    @PutMapping("/re-sendmail")
    public ResponseEntity<String> reSendMail(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        memberService.sendMail(memberAdapter.getMember());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/withoutPasswordLogin")
    public ResponseEntity<String> withoutPasswordLoginSendMail(@RequestBody String email) {
        log.info("email : {} ",email);

        try {
            memberService.loginSendMail(email);
        } catch (UsernameNotFoundException e) {
            log.error("error : {} ", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/member/password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                 @RequestBody @Valid ChangePasswordRequest changePasswordRequest,
                                                 Errors errors) {
        if (errors.hasErrors()) {
            log.error("errors : {} ", errors);
            return new ResponseEntity<>(errors.getFieldError().getField(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        memberService.changePassword(changePasswordRequest, memberAdapter.getMember());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
