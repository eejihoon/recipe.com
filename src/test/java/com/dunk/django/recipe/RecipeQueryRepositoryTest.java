package com.dunk.django.recipe;

import com.dunk.django.domain.Ingredient;
import com.dunk.django.domain.Recipe;
import com.dunk.django.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

@Transactional
@SpringBootTest
class RecipeQueryRepositoryTest {
    @Autowired RecipeQueryRepository recipeQueryRepository;
    @Autowired RecipeRepository recipeRepository;

    @BeforeEach
    void addRecipe() {
        for (int i = 0; i < 100; i++) {
            Recipe recipe = Recipe.builder()
                    .thumbnail("test")
                    .title("recipe-title!!!!!")
                    .fullDescription("test")
                    .description("test")
                    .ingredients(new HashSet<Ingredient>(Arrays.asList(new Ingredient("a"), new Ingredient("b"), new Ingredient("c"))))
                    .cookingTime(11)
                    .build();

            recipeRepository.save(recipe);
        }
    }

    @AfterEach void deleteAll() {
        recipeRepository.deleteAll();
    }

    @DisplayName("QueryDSL 레시피 검색")
    @Test
    void testFindByKeyword() {
        testFindByKeyword("ci");
    }

    @DisplayName("QueryDSL 레시피 null 검색")
    @Test
    void testFindByKeywordFailure() {
        testFindByKeyword(null);
    }

    private void testFindByKeyword(String keyword) {
        Page<Recipe> test = recipeQueryRepository.findByRecipeTitle(null, PageRequest.of(0, 10, Sort.by("id").descending()));

        Recipe recipe = recipeRepository.findAll().get(0);

        assertEquals(test.getTotalElements(), 100);
        assertEquals(test.getContent().size(), 10);
        assertEquals(test.getContent().get(0).getTitle(), recipe.getTitle());
    }



}