package com.recipe.member.dto;

import com.recipe.member.domain.Member;
import com.recipe.member.domain.Role;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
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

    public Member toEntity() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(encoder.encode(password))
                .role(Role.USER)
                .authenticationKey(UUID.randomUUID().toString())
                .build();
    }
}
