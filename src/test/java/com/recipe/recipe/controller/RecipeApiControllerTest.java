package com.recipe.recipe.controller;

import com.recipe.TestSet;
import com.recipe.recipe.domain.Ingredient;
import com.recipe.recipe.domain.Recipe;
import com.recipe.member.WithMockCutstomUser;
import com.recipe.recipe.dto.RecipeSaveForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCutstomUser
@Transactional
@SpringBootTest
public class RecipeApiControllerTest extends TestSet {
    
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
                .andExpect(status().isCreated());

        //then
        List<Recipe> recipes = recipeRepository.findAll();

        assertTrue(recipes.get(0).getIngredients().size() > 0);
        assertEquals(recipes.get(0).getTitle(), recipeSaveForm.getTitle());
        assertEquals(recipes.get(0).getMember().getEmail(), USER_EMAIL);
    }

    @DisplayName("레시피 등록 테스트 실패 - 제목&설명에 공백 입력")
    @WithMockCutstomUser
    @Test
    void testRecipeInsertFailure() throws Exception {
        //given
        RecipeSaveForm recipeSaveForm = wrongRecipeSaveForm();

        String requesetJson = objectMapper.writeValueAsString(recipeSaveForm);

        //when
        mockMvc.perform(post("/api/recipe")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .content(requesetJson))
                .andExpect(status().isBadRequest());

        //then
        List<Recipe> recipes = recipeRepository.findAll();

        assertEquals(recipes.size(), 0);
    }

    @DisplayName("레시피 등록 테스트 실패 - 제목&설명에 공백 입력")
    @WithMockCutstomUser
    @Test
    void testRecipeInsertFailure2() throws Exception {
        //given
        RecipeSaveForm recipeSaveForm = RecipeSaveForm.builder()
                .title("")
                .fullDescription("")
                .ingredients("a,b,c,d")
                .description("...")
                .build();

        String requesetJson = objectMapper.writeValueAsString(recipeSaveForm);

        //when
        mockMvc.perform(post("/api/recipe")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .content(requesetJson))
                .andExpect(status().isBadRequest());

        //then
        List<Recipe> recipes = recipeRepository.findAll();

        assertEquals(recipes.size(), 0);
    }

    @DisplayName("레시피 수정 테스트")
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

    @DisplayName("레시피 수정 실패 테스트 - 제목, 설명에 공백")
    @Test
    void testRecipeModifyFailure() throws Exception {
        //given
        Recipe recipe = addRecipe();
        RecipeSaveForm recipeUpdateForm = wrongRecipeSaveForm();

        //when
        mockMvc.perform(put("/api/recipe/"+recipe.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(recipeUpdateForm))
                .with(csrf()))
                .andExpect(status().isNotModified());

        //then
        Recipe updatedRecipe = recipeRepository.findWithAllById(recipe.getId());

        assertNotEquals(updatedRecipe.getTitle(), recipeUpdateForm.getTitle());
        assertNotEquals(updatedRecipe.getFullDescription(), recipeUpdateForm.getFullDescription());
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

    private RecipeSaveForm wrongRecipeSaveForm() {
        return RecipeSaveForm.builder()
                .title("")
                .fullDescription("")
                .ingredients("a,b,c,d")
                .member(getMember())
                .description("...")
                .build();
    }

}
