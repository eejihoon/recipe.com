package com.dunk.django.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class SignupRequestValidator implements Validator {
    private final MemberRepository memberRepository;

    //어떤 타입 인스턴스를 검증할 것인지 명시
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignupRequestDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupRequestDto signupRequestDto = (SignupRequestDto) target;

        //아이디 중복 체크
        if (existAccount(signupRequestDto)) {
            errors.rejectValue("account", "invalid.account", new Object[]{signupRequestDto.getAccount()},"이미 가입한 사용자입니다.");
        }

        //비밀번호 일치하는지 체크
        if (!equalsPassword(signupRequestDto)) {
            errors.rejectValue("confirmPassword", "wrong.value", "비밀번호가 일치하지 않습니다.");
        }
    }

    private boolean equalsPassword(SignupRequestDto signupRequestDto) {
        return signupRequestDto.password.equals(signupRequestDto.getConfirmPassword());
    }

    private boolean existAccount(SignupRequestDto signupRequestDto) {
        return memberRepository.existsByAccount(signupRequestDto.getAccount());
    }


}
