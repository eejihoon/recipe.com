package com.dunk.django.member;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
*   참고
*   https://docs.spring.io/spring-security/site/docs/5.2.x/reference/html/test.html#test-method-withsecuritycontext
*
* */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCutstomUserSecurityContextFactory.class)
public @interface WithMockCutstomUser {
    String email() default "test@email.com";
    String nickname() default "testNick";
}
