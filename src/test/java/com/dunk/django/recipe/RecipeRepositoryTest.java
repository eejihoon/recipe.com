package com.dunk.django.recipe;

import com.dunk.django.domain.*;
import com.dunk.django.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class RecipeRepositoryTest {

    @Autowired
    RecipeRepository recipeRepository;

    @DisplayName("RecipeRepository - 등록 테스트")
    @Test
    void testSaveRecipe() {
        List<Ingredient> ingredientList = new ArrayList<>();
        List<CookingMethod> cookingMethodList = new ArrayList<>();

        ingredientList.add(new Ingredient());
        cookingMethodList.add(new CookingMethod());

        Recipe newRecipe = Recipe.builder()
                .name("콩비지동그랑땡")
                .description("두부대신 콩비지를 넣어 만든 동그랑땡 맛도 좋아요!")
                .cookingTime(30)
                .servings(3)
                .thumbnail("http://file.okdab.com/recipe/148299577268400131.jpg")
                .foodNation(new FoodNation("한식"))
                .foodType(new FoodType("반찬"))
                .ingredients(ingredientList)
                .cookingMethods(cookingMethodList)
                .build();

        Recipe saveRecipe = recipeRepository.save(newRecipe);

        Recipe findRecipe = recipeRepository.findAll().get(0);

        assertEquals(saveRecipe.getId(), findRecipe.getId());


    }

}