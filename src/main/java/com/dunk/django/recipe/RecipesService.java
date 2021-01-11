package com.dunk.django.recipe;

import java.util.List;

import javax.transaction.Transactional;

import com.dunk.django.domain.Recipes;

@Transactional
public interface RecipesService {

    Long register(RecipeDTO dto);

    Recipes get(Long bno);

    // Page<Recipe> getList(Pageable pageable);
    GenericListDTO<RecipeDTO, Recipes> getList(PageDTO pageDTO);

    Long modify(RecipeDTO dto);

    void remove(Long recipe_no);

    // 추천리스트 뽑기
    List<Recipes> getRecommendList(String userId);

    // DTO객체를 Entity(VO)객체로 바꾼다.
    default Recipes bindToEntity(RecipeDTO dto) {
        Recipes entity = Recipes.builder().itemId(dto.getItemId()).recipe_name(dto.getRecipe_name())
                .ingr_list(dto.getIngr_list()).recipe(dto.getRecipe()).img(dto.getImg()).build();

        return entity;
    }

    default RecipeDTO bindToDTO(Recipes entity) {
        RecipeDTO dto = RecipeDTO.builder().itemId(entity.getItemId()).recipe_name(entity.getRecipe_name())
                .ingr_list(entity.getIngr_list()).recipe(entity.getRecipe()).img(entity.getImg()).build();

        return dto;
    }

}