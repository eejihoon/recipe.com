package com.dunk.django.config;

import com.dunk.django.service.MemberCheckService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailServie() {
        return new MemberCheckService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailServie());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        // http.authorizeRequests().antMatchers("/sample/signup").permitAll();
        // http.authorizeRequests().antMatchers("/sample/all").permitAll();
        // http.authorizeRequests().antMatchers("/sample/member").hasRole("MEMBER");
        // http.authorizeRequests().antMatchers("/sample/admin").hasRole("ADMIN");
        
        /* Custom Login , Logout , AccessDenied */
        http.formLogin().loginPage("/member/login").defaultSuccessUrl("/django/index  ", true);

        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
        .logoutSuccessUrl("/django/index").invalidateHttpSession(true);
        
        http.exceptionHandling().accessDeniedPage("/member/denied");
        
        http.csrf().disable();

    }
}