package com.recipe.recipe.repository;

import com.recipe.domain.FoodNation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FoodNationRepository extends JpaRepository<FoodNation, Long> {
}
