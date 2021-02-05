package com.dunk.django.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO csrf 활성화 및 각 페이지 권한 설정
        http.csrf().disable();

        http.authorizeRequests()
                .mvcMatchers("/node_modules/**", "/error", "/img/**", "/profile", "/like/**", "/scrap",
                        "/", "/login", "/signup", "/recipe", "/withoutPasswordLogin", "/member/withoutPasswordLogin").permitAll()
                .mvcMatchers("/register", "/modify/**", "/member/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .permitAll();

        http.logout()
                .logoutSuccessUrl("/");

        http.rememberMe()
                .userDetailsService(userDetailsService)
                .tokenRepository(tokenRepository());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }


    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}