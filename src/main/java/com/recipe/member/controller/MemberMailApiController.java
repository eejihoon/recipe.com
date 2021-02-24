package com.recipe.member.controller;

import com.recipe.config.security.LoginMember;
import com.recipe.member.domain.Member;
import com.recipe.member.service.MemberMailService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberMailApiController {
    private final MemberMailService memberMailService;
    private final static String API = "/api";

    @ApiOperation(value = "회원가입 인증 메일 재전송")
    @PutMapping(API+"/re-sendmail")
    public ResponseEntity<String> reSendMail(@LoginMember Member loginMember) {
        memberMailService.sendMail(loginMember);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 분실한 회원 계정으로 메일 전송")
    @PutMapping(API+"/withoutPasswordLogin")
    public ResponseEntity<String> withoutPasswordLoginSendMail(@RequestBody String email) {
        log.info("email : {} ",email);

        try {
            memberMailService.loginSendMail(email);
        } catch (UsernameNotFoundException | DisabledException e) {
            log.error("error : {} ", e);

            return new ResponseEntity<>
                    ("존재하지 않는 계정입니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
