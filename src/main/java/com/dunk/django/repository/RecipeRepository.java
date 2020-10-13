package com.dunk.django.repository;

import com.dunk.django.domain.Recipe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // update SingleBoard b set b.title =:title, b.moddate = :moddate where b.sno
    // =:sno"
    // @Modifying
    // @Transactional
    // @Query("update tbl_recipe r set r.recipe_name =:recipe_name,
    // r.ingr_list=:ingr_list, r.recipe =:recipe where r.recipe_no = :recipe_no")
    // int update(@Param("rno")Long rno, @Param("recipe_name")String recipe_name,
    // @Param("ingr_list")String ingr_list, @Param("recipe")String recipe);
    Recipe findByItemId(Long itemId);
}