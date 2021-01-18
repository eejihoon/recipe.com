package com.dunk.django.recipe;

import com.dunk.django.domain.*;
import com.dunk.django.recipe.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class RecipeControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired RecipeRepository recipeRepository;
    @Autowired IngredientTypeRepository ingredientTypeRepository;
    @Autowired IngredientRepository ingredientRepository;
    @Autowired ObjectMapper objectMapper;

    @DisplayName("recipe index page")
    @Test
    void testRecipeIndex() throws Exception {
        mockMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipes"))
                .andDo(print());
    }

    @DisplayName("레시피 등록 테스트")
    @Test
    void testRecipeSave() throws Exception {
        RecipeSaveForm recipeSaveForm = new RecipeSaveForm();
        recipeSaveForm.setTitle("Test Recipe!!");
        recipeSaveForm.setFullDescription("full Description!");
        recipeSaveForm.setIngredients("재료,이렇게,들어와,콤마로,구문해,태그를,썼기,때문이야");
        recipeSaveForm.setDescription("Test Description");
        recipeSaveForm.setThumbnail("image URL");
        recipeSaveForm.setCookingTime(20);

        String requesetJson = objectMapper.writeValueAsString(recipeSaveForm);
        System.out.println("requesetJson : " + requesetJson);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .content(requesetJson))
                .andExpect(status().isOk());

        List<Recipe> recipes = recipeRepository.findAll();

        recipes.get(0).getIngredients().forEach(row -> System.out.println(row.getIngredient()));

        assertTrue(recipes.get(0).getIngredients().size() > 0);
        assertEquals(recipes.get(0).getTitle(), recipeSaveForm.getTitle());
    }



}