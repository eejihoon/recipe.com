package com.recipe.recipe.repository;

import com.recipe.domain.IngredientType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientTypeRepository extends JpaRepository<IngredientType, Long> {
}
