package com.dunk.django.recipe;


import com.dunk.django.recipe.repository.IngredientRepository;
import com.dunk.django.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @GetMapping("/recipe")
    public String findAll(Long id, Model model) {

        log.info("id : {}", id);
        model.addAttribute("recipe", recipeRepository.findWithAllById(id));

        return "recipe/recipe";
    }
}