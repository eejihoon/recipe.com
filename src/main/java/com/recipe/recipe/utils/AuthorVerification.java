package com.recipe.recipe.utils;

import com.recipe.domain.Member;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

/*
*   작업을 요청한 사용자와 게시물 작성자가 같은 사용자인지 판별하는 객체
* */
@Component
public class AuthorVerification {
    public void isAuthor(Member currentMember, Member Author) throws AccessDeniedException {
        if (currentMember.getEmail().equals(Author.getEmail()))
            return;

        throw new AccessDeniedException("권한이 없는 사용자가 접근했습니다.");
    }
}
