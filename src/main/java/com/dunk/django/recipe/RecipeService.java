package com.dunk.django.recipe;

import com.dunk.django.domain.Ingredient;
import com.dunk.django.domain.Recipe;
import com.dunk.django.recipe.repository.IngredientRepository;
import com.dunk.django.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public Long save(RecipeSaveForm recipeSaveForm) {
        Recipe saveRecipe = recipeRepository.save(recipeSaveForm.toEntity());

        return saveRecipe.getId();
    }

    public void update(RecipeSaveForm recipeSaveForm, Long id) {
        Recipe recipe = recipeRepository.findWithAllById(id);

        recipe.update(recipeSaveForm.toEntity());
    }

    public RecipeSaveForm getRecipeForm(Long id) {
        Recipe findedRecipe = recipeRepository.findWithAllById(id);

        return RecipeSaveForm.builder()
                .id(id)
                .thumbnail(findedRecipe.getThumbnail())
                .title(findedRecipe.getTitle())
                .description(findedRecipe.getDescription())
                .fullDescription(findedRecipe.getFullDescription())
                .ingredients(findedRecipe.getIngredientsString())
                .cookingTime(findedRecipe.getCookingTime())
                .build();
    }
}