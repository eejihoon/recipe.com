package com.recipe.recipe.utils;

import com.recipe.member.domain.Member;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

/*
 *   작업을 요청한 사용자와 게시물 작성자가 같은 사용자인지 판별하는 객체
 * */
@Component
public class AuthorVerification {
    public boolean isAuthor(Member loginMember, Member Author) {
        if (Objects.nonNull(loginMember))
            return loginMember.getEmail().equals(Author.getEmail());

        return false;
    }
}
