package com.dunk.django.recipe;

import com.dunk.django.domain.Recipe;
import com.dunk.django.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public Long save(RecipeSaveForm recipeSaveForm) {
        Recipe saveRecipe = recipeRepository.save(recipeSaveForm.toEntity());
        return saveRecipe.getId();
    }
}