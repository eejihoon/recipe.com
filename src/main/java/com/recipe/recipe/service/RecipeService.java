package com.recipe.recipe.service;

import com.recipe.member.domain.Member;
import com.recipe.recipe.domain.Recipe;
import com.recipe.recipe.dto.RecipeSaveForm;
import com.recipe.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

import static com.recipe.recipe.utils.AuthorVerification.isAuthor;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public Long save(RecipeSaveForm recipeSaveForm, Member member) {
        recipeSaveForm.setMember(member);
        Recipe saveRecipe = recipeRepository.save(recipeSaveForm.toEntity());

        return saveRecipe.getId();
    }

    public void update(RecipeSaveForm recipeSaveForm, Long updateTargetId, Member currentMember) {
        Recipe recipe = recipeRepository.findWithAllById(updateTargetId);

        isAuthor(currentMember, recipeSaveForm.getMember());

        recipe.update(recipeSaveForm.toEntity());
    }

    public RecipeSaveForm getRecipeModifyForm(Long id, Member loginMember) {
        Recipe findedRecipe = recipeRepository.findWithAllById(id);

        //접근한 사용자가 작성자가 맞는 지 체크
        isAuthor(loginMember, findedRecipe.getMember());

        return new RecipeSaveForm(findedRecipe);
    }

    public void remove(Long id, Member currentMember) {
        Recipe removeTarget = recipeRepository.findById(id).orElseThrow();
        isAuthor(currentMember, removeTarget.getMember());
        recipeRepository.delete(removeTarget);
    }

    public Recipe findRecipe(Long id, Member loginMember) throws AccessDeniedException {
        Recipe findByRecipe = recipeRepository.findWithAllById(id);
        viewCount(findByRecipe, loginMember);

        return findByRecipe;
    }

    private void viewCount(Recipe recipe, Member loginMember) throws AccessDeniedException {
        //내가 쓴 게시물이라면 조회수 증가하지 않는다.
        if (isAuthor(loginMember, recipe.getMember())) return;

        recipe.viewCount();
    }
}