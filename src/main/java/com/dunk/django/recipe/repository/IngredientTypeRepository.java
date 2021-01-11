package com.dunk.django.recipe.repository;

import com.dunk.django.domain.IngredientType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientTypeRepository extends JpaRepository<IngredientType, Long> {
}
