package com.recipe.recipe.repository;

import com.recipe.recipe.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"ingredients", "cookingMethods", "member"})
    Recipe findWithAllById(Long id);
}
