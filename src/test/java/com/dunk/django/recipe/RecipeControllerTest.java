package com.dunk.django.recipe;

import com.dunk.django.domain.*;
import com.dunk.django.recipe.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.text.LayoutQueue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Autowired FoodNationRepository foodNationRepository;
    @Autowired FoodTypeRepository foodTypeRepository;
    @Autowired IngredientTypeRepository ingredientTypeRepository;
    @Autowired IngredientRepository ingredientRepository;
    @Autowired CookingMethodRepository cookingMethodRepository;
    @Autowired ObjectMapper objectMapper;

    @DisplayName("recipe index page")
    @Test
    void testRecipeIndex() throws Exception {
        mockMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipes"))
                .andDo(print());
    }

    @DisplayName("레시피 등록 스트")
    @Test
    void testRecipeSave() throws Exception {
        FoodType foodType = new FoodType("test food type");
        FoodNation foodNation = new FoodNation("test nation");
        IngredientType ingredientType = new IngredientType("test ingredient type");

        ingredientTypeRepository.save(ingredientType);
        foodNationRepository.save(foodNation);
        foodTypeRepository.save(foodType);

        List<FoodNation> allNation = foodNationRepository.findAll();
        List<FoodType> allTypes = foodTypeRepository.findAll();
        List<IngredientType> allIngredientTypes = ingredientTypeRepository.findAll();
        Set<Ingredient> ingredients = new HashSet<>();
        Set<CookingMethod> cookingMethods = new HashSet<>();

//        for (int i=0; i < 10; i++) {
//            Ingredient ingredient = Ingredient.builder()
//                    .ingredient(i+"-test")
//                    .quantity(i+"g")
//                    .ingredientType(allIngredientTypes.get(0))
//                    .build();
//
//            CookingMethod cookingMethod = CookingMethod.builder()
//                    .description(i+" method des")
//                    .image(i+" method Image")
//                    .sequence(i)
//                    .build();
//
//            ingredients.add(ingredient);
//            cookingMethods.add(cookingMethod);
//        }

        RecipeSaveForm recipeSaveForm = new RecipeSaveForm();
        recipeSaveForm.setTitle("Test Recipe!!");
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

        System.out.println(recipes.get(0).getCookingMethods().isEmpty());
        recipes.get(0).getCookingMethods().forEach(row -> System.out.println(row.getDescription()));

        assertTrue(!recipes.get(0).getCookingMethods().isEmpty());
        assertTrue(!recipes.get(0).getIngredients().isEmpty());
        assertEquals(recipes.get(0).getTitle(), recipeSaveForm.getTitle());

    }



}