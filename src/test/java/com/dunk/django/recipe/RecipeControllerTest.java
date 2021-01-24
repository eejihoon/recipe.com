package com.dunk.django.recipe;

import com.dunk.django.domain.*;
import com.dunk.django.recipe.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class RecipeControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired RecipeRepository recipeRepository;
    @Autowired IngredientTypeRepository ingredientTypeRepository;
    @Autowired IngredientRepository ingredientRepository;
    @Autowired ObjectMapper objectMapper;

    @BeforeEach
    void deleteAll() {
        recipeRepository.deleteAll();
    }

    @DisplayName("레시피 메인 폼")
    @Test
    void testRecipeIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipes"))
                .andDo(print());
    }

    @DisplayName("레시피 수정 폼")
    @WithMockUser
    @Test
    void testRecipeModify() throws Exception {
        Recipe recipe = addRecipe();

        mockMvc.perform(get("/modify/"+recipe.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));
    }


    @DisplayName("레시피 검색")
    @Test
    void testFindByTitle() throws Exception {
       for (int i=0;i<10;i++) recipeRepository.save(addRecipe());

       mockMvc.perform(get("/")
       .param("title","recipe"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("recipes"));

    }

    private Recipe addRecipe() {
        Recipe recipe = Recipe.builder()
                .thumbnail("test")
                .title("test-recipe")
                .fullDescription("test")
                .description("test")
                .ingredients(new HashSet<Ingredient>(Arrays.asList(new Ingredient("a"), new Ingredient("b"), new Ingredient("c"))))
                .cookingTime(11)
                .build();

        Recipe save = recipeRepository.save(recipe);

        return save;
    }


}