package com.recipe.member.controller;

import com.recipe.member.domain.Member;
import com.recipe.member.dto.ChangePasswordRequest;
import com.recipe.member.dto.SignupRequest;
import com.recipe.member.service.MemberMailService;
import com.recipe.member.service.MemberService;
import com.recipe.config.security.LoginMember;
import com.recipe.member.vaildation.ChangePasswordRequestValidator;
import com.recipe.member.vaildation.SignupRequestValidator;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberApiController {
    private final MemberService memberService;
    private final SignupRequestValidator signupRequestValidator;
    private final ChangePasswordRequestValidator changePasswordRequestValidator;

    private final String MEMBER_URL = "/api/member";

    @InitBinder("signupRequest")
    public void signupInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signupRequestValidator);
    }

    @InitBinder("changePasswordRequest")
    public void changePasswordInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(changePasswordRequestValidator);
    }

    @ApiOperation(value = "회원 가입")
    @PostMapping(MEMBER_URL)
    public ResponseEntity<String> signupSubmit(@RequestBody @Valid SignupRequest signupRequest,
                                               Errors errors) {
        if (errors.hasErrors()) {
            log.info(errors.getFieldError().getField());
            return new ResponseEntity<>
                    (errors.getFieldError().getField(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String nickname = memberService.signup(signupRequest);

        return new ResponseEntity<>(nickname, HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 변경")
    @PutMapping(MEMBER_URL)
    public ResponseEntity<String> changePassword(@LoginMember Member loginMember,
                                                 @RequestBody @Valid ChangePasswordRequest changePasswordRequest,
                                                 Errors errors) {
        if (errors.hasErrors()) {
            log.error("errors : {} ", errors);
            return new ResponseEntity<>
                    (errors.getFieldError().getField(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        memberService.changePassword(changePasswordRequest, loginMember);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "회원탈퇴")
    @DeleteMapping(MEMBER_URL)
    public ResponseEntity<String> disableMember(@LoginMember Member loginMember,
                                                @RequestBody String password) {
        boolean result = false;

        if (Objects.nonNull(loginMember)) {
            result = memberService.disableMember(loginMember, password);
        }

        return result ?
                new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
