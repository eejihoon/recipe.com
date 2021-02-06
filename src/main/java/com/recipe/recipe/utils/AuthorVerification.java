package com.recipe.recipe.utils;

import com.recipe.domain.Member;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class AuthorVerification {
    public void isAuthor(Member currentMember, Member Author) throws AccessDeniedException {
        if (currentMember.getEmail().equals(Author.getEmail()))
            return;

        throw new AccessDeniedException("권한이 없는 사용자가 접근했습니다.");
    }
}
