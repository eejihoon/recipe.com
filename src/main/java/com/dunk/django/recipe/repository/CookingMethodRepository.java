package com.dunk.django.recipe.repository;

import com.dunk.django.domain.CookingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookingMethodRepository extends JpaRepository<CookingMethod, Long> {
}
