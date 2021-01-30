package com.dunk.django.recipe;

import com.dunk.django.domain.Ingredient;
import com.dunk.django.domain.Member;
import com.dunk.django.domain.Recipe;
import com.dunk.django.member.MemberAdapter;
import com.dunk.django.recipe.repository.IngredientRepository;
import com.dunk.django.recipe.repository.RecipeRepository;
import com.dunk.django.recipe.utils.AuthorVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final AuthorVerification authorVerification;

    public Long save(RecipeSaveForm recipeSaveForm, Member member) {
        recipeSaveForm.setMember(member);
        Recipe saveRecipe = recipeRepository.save(recipeSaveForm.toEntity());

        return saveRecipe.getId();
    }

    public void update(RecipeSaveForm recipeSaveForm, Long updateTargetId, Member currentMember) throws AccessDeniedException {
        Recipe recipe = recipeRepository.findWithAllById(updateTargetId);

        authorVerification.isAuthor(currentMember, recipeSaveForm.getMember());

        recipe.update(recipeSaveForm.toEntity());
    }

    public RecipeSaveForm getRecipeForm(Long id, Member currentMember) throws AccessDeniedException {
        Recipe findedRecipe = recipeRepository.findWithAllById(id);

        //접근한 사용자가 작성자가 맞는 지 체크
        authorVerification.isAuthor(currentMember, findedRecipe.getMember());
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

    public Recipe findRecipe(Long id, MemberAdapter memberAdapter) {
        Recipe findedRecipe = recipeRepository.findWithAllById(id);

        if (memberAdapter != null) {
            boolean isNotMine = !memberAdapter.getMember().getEmail().equals(findedRecipe.getMember().getEmail());
            if (isNotMine) {
                findedRecipe.viewCount();
            }
        } else {
            findedRecipe.viewCount();
        }

        return findedRecipe;
    }
}