package com.dunk.django.service;

import javax.transaction.Transactional;

import com.dunk.django.domain.Recipe;
import com.dunk.django.dto.GenericListDTO;
import com.dunk.django.dto.PageDTO;
import com.dunk.django.dto.RecipeDTO;

@Transactional
public interface RecipeService {
    
    Long register(RecipeDTO dto);
    
    Recipe get(Long bno);

    // Page<Recipe> getList(Pageable pageable);
    GenericListDTO<RecipeDTO, Recipe> getList(PageDTO pageDTO);

    Long modify(RecipeDTO dto);
    
    void remove(Long recipe_no);

        //DTO객체를 Entity(VO)객체로 바꾼다.
        default Recipe bindToEntity(RecipeDTO dto) {
            Recipe entity = Recipe.builder()
            .itemId(dto.getItemId())
            .recipe_name(dto.getRecipe_name())
            .ingr_list(dto.getIngr_list())
            .recipe(dto.getRecipe())
            .build();

            return entity;
        }
    
        default RecipeDTO bindToDTO(Recipe entity) {
            RecipeDTO dto = RecipeDTO.builder()
            .itemId(entity.getItemId())
            .recipe_name(entity.getRecipe_name())
            .ingr_list(entity.getIngr_list())
            .recipe(entity.getRecipe())
            .build();

            return dto;
        }

}