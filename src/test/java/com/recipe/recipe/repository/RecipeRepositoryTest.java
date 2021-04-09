package com.recipe.recipe.repository;

import com.recipe.recipe.domain.CookingMethod;
import com.recipe.recipe.domain.Ingredient;
import com.recipe.recipe.domain.Recipe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class RecipeRepositoryTest {
    @Autowired
    RecipeRepository recipeRepository;

    @DisplayName("RecipeRepository - 등록 테스트")
    @Transactional
    @Test
    void testSaveRecipe() {
        Set<Ingredient> ingredientList = new HashSet<>();
        Set<CookingMethod> cookingMethodList = new HashSet<>();

        ingredientList.add(new Ingredient());
        cookingMethodList.add(new CookingMethod());

        Recipe newRecipe = Recipe.builder()
                .title("콩비지동그랑땡")
                .description("두부대신 콩비지를 넣어 만든 동그랑땡 맛도 좋아요!")
                .cookingTime(30)
                .thumbnail("http://file.okdab.com/recipe/148299577268400131.jpg")
                .build();

        Recipe saveRecipe = recipeRepository.save(newRecipe);

        Recipe findRecipe = recipeRepository.findAll().get(0);

        assertEquals(saveRecipe.getId(), findRecipe.getId());
    }

}