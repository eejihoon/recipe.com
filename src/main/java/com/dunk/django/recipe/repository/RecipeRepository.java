package com.dunk.django.recipe.repository;

import com.dunk.django.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
