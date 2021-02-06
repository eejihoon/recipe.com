package com.recipe.recipe.repository;

import com.recipe.domain.CookingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookingMethodRepository extends JpaRepository<CookingMethod, Long> {
}
