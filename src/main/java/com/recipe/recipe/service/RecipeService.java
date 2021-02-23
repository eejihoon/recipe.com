package com.recipe.recipe.service;

import com.recipe.member.domain.Member;
import com.recipe.recipe.domain.Recipe;
import com.recipe.member.utils.MemberAdapter;
import com.recipe.recipe.dto.RecipeSaveForm;
import com.recipe.recipe.repository.RecipeRepository;
import com.recipe.recipe.utils.AuthorVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final AuthorVerification authorVerification;

    //등록
    public Long save(RecipeSaveForm recipeSaveForm, Member member) {
        recipeSaveForm.setMember(member);
        Recipe saveRecipe = recipeRepository.save(recipeSaveForm.toEntity());

        return saveRecipe.getId();
    }

    //수정
    public void update(RecipeSaveForm recipeSaveForm, Long updateTargetId, Member currentMember) throws AccessDeniedException {
        Recipe recipe = recipeRepository.findWithAllById(updateTargetId);

        authorVerification.isAuthor(currentMember, recipeSaveForm.getMember());

        recipe.update(recipeSaveForm.toEntity());
    }

    //수정 폼
    public RecipeSaveForm getRecipeModifyForm(Long id, Member loginMember) throws AccessDeniedException {
        Recipe findedRecipe = recipeRepository.findWithAllById(id);

        //접근한 사용자가 작성자가 맞는 지 체크
        authorVerification.isAuthor(loginMember, findedRecipe.getMember());
        return RecipeSaveForm.builder()
                .id(id)
                .thumbnail(findedRecipe.getThumbnail())
                .title(findedRecipe.getTitle())
                .description(findedRecipe.getDescription())
                .fullDescription(findedRecipe.getFullDescription())
                .ingredients(findedRecipe.getIngredientsString())
                .cookingTime(findedRecipe.getCookingTime())
                .member(findedRecipe.getMember())
                .build();
    }

    public void remove(Long id, Member currentMember) throws AccessDeniedException {
        Recipe removeTarget = recipeRepository.findById(id).orElseThrow();

        authorVerification.isAuthor(currentMember, removeTarget.getMember());


        recipeRepository.delete(removeTarget);
    }

    public Recipe findRecipe(Long id, Member loginMember) throws AccessDeniedException {
        Recipe findByRecipe = recipeRepository.findWithAllById(id);
        viewCount(findByRecipe, loginMember);

        return findByRecipe;
    }

    private void viewCount(Recipe recipe, Member loginMember) throws AccessDeniedException {
        //내가 쓴 게시물이라면 조회수 증가하지 않는다.
        if (authorVerification
                .isAuthor(loginMember, recipe.getMember())) return;

        recipe.viewCount();
    }
}