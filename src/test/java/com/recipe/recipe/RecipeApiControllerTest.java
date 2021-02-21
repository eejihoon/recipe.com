package com.recipe.recipe;

import com.recipe.ControllerTest;
import com.recipe.domain.Ingredient;
import com.recipe.domain.Recipe;
import com.recipe.member.LikeRepository;
import com.recipe.member.MemberRepository;
import com.recipe.member.WithMockCutstomUser;
import com.recipe.recipe.repository.IngredientRepository;
import com.recipe.recipe.repository.IngredientTypeRepository;
import com.recipe.recipe.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCutstomUser
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class RecipeApiControllerTest extends ControllerTest {
    
    @DisplayName("레시피 등록 테스트")
    @WithMockCutstomUser
    @Test
    void testRecipeSave() throws Exception {
        //given
        RecipeSaveForm recipeSaveForm = getRecipeSaveForm();
        String requesetJson = objectMapper.writeValueAsString(recipeSaveForm);

        //when
        mockMvc.perform(post("/api/recipe")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .content(requesetJson))
                .andExpect(status().isOk());

        //then
        List<Recipe> recipes = recipeRepository.findAll();

        assertTrue(recipes.get(0).getIngredients().size() > 0);
        assertEquals(recipes.get(0).getTitle(), recipeSaveForm.getTitle());
        assertEquals(recipes.get(0).getMember().getEmail(), USER_EMAIL);
    }

    @DisplayName("레시피 수정 처리")
    @Test
    void testRecipeModifyPut() throws Exception {
        //given
        Recipe recipe = addRecipe();
        RecipeSaveForm recipeUpdateForm = getRecipeSaveForm();

        //when
        mockMvc.perform(put("/api/recipe/"+recipe.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(recipeUpdateForm))
                .with(csrf()))
                .andExpect(status().isOk());

        //then
        Recipe updatedRecipe = recipeRepository.findWithAllById(recipe.getId());

        assertEquals(updatedRecipe.getTitle(), recipeUpdateForm.getTitle());
        assertEquals(updatedRecipe.getThumbnail(), recipeUpdateForm.getThumbnail());
        assertEquals(updatedRecipe.getFullDescription(), recipeUpdateForm.getFullDescription());
        assertEquals(updatedRecipe.getIngredients().size(), recipeUpdateForm.toEntity().getIngredients().size());
        assertEquals(recipe.getMember().getEmail(), USER_EMAIL);

    }

    @DisplayName("레시피 삭제")
    @Test
    void testRemoveRecipe() throws Exception {
        //given
        Recipe recipe = addRecipe();
        assertTrue(recipeRepository.findById(recipe.getId()).isPresent());

        //when
        mockMvc.perform(delete("/api/recipe/"+recipe.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        //then
        assertFalse(recipeRepository.findById(recipe.getId()).isPresent());
        assertEquals(recipe.getMember().getEmail(), USER_EMAIL);
    }

}
