package com.dunk.django.recipe.utils;

import com.dunk.django.domain.Member;
import com.dunk.django.member.MemberAdapter;
import com.dunk.django.recipe.RecipeSaveForm;
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
