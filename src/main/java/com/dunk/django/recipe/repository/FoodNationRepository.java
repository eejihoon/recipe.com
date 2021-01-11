package com.dunk.django.recipe.repository;

import com.dunk.django.domain.FoodNation;
import com.dunk.django.domain.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FoodNationRepository extends JpaRepository<FoodNation, Long> {
}
