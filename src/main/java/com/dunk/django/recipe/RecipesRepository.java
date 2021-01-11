package com.dunk.django.recipe;

import com.dunk.django.domain.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipesRepository extends JpaRepository<Recipes, Long> {

    // update SingleBoard b set b.title =:title, b.moddate = :moddate where b.sno
    // =:sno"
    // @Modifying
    // @Transactional
    // @Query("update tbl_recipe r set r.recipe_name =:recipe_name,
    // r.ingr_list=:ingr_list, r.recipe =:recipe where r.recipe_no = :recipe_no")
    // int update(@Param("rno")Long rno, @Param("recipe_name")String recipe_name,
    // @Param("ingr_list")String ingr_list, @Param("recipe")String recipe);
    Recipes findByItemId(Long itemId);
}