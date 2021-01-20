package com.dunk.django;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
public class QueryDslTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    void testConnectQueryDsl() {
        System.out.println(jpaQueryFactory);
        assertNotNull(jpaQueryFactory);
    }
}
