package com.dunk.django.member;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
public class SignupRequest {

    @Length(min = 4)
    @Email
    @NotBlank
    String email;

    @Length(min = 2, max = 12)
    @NotBlank
    String nickname;

    @Length(min = 8)
    @NotBlank
    String password;

    @Length(min = 8, max = 16)
    @NotBlank
    String confirmPassword;
}
