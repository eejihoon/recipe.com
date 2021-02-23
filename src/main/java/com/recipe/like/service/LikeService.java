package com.recipe.like.service;

import com.recipe.like.domain.Like;
import com.recipe.member.domain.Member;
import com.recipe.recipe.domain.Recipe;
import com.recipe.like.repository.LikeRepository;
import com.recipe.member.utils.MemberAdapter;
import com.recipe.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final RecipeRepository recipeRepository;

    public boolean addLike(Member member, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();

        //중복 좋아요 방지
        if(isNotAlreadyLike(member, recipe)) {
            likeRepository.save(new Like(recipe, member));
            return true;
        }

        return false;
    }

    //사용자가 이미 좋아요 한 게시물인지 체크
    private boolean isNotAlreadyLike(Member member, Recipe recipe) {
        return likeRepository.findByMemberAndRecipe(member, recipe).isEmpty();
    }

    public void cancelLike(Member member, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        Like like = likeRepository.findByMemberAndRecipe(member,recipe).orElseThrow();
        likeRepository.delete(like);
    }

    public List<String> count(Long recipeId, Member loginMember) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();

        Integer recipeLikeCount = likeRepository.countByRecipe(recipe).orElse(0);

        List<String> resultData = new ArrayList<>(Arrays.asList(String.valueOf(recipeLikeCount)));

        if (Objects.nonNull(loginMember)) {
            boolean notAlreadyLike = isNotAlreadyLike(loginMember, recipe);
            resultData.add(String.valueOf(notAlreadyLike));
            return resultData;
        }

        return resultData;
    }
}