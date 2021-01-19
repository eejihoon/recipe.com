package com.dunk.django.recipe;

import com.dunk.django.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;

    @GetMapping("/recipe")
    public String findAll(Long id, Model model) {
        log.info("id : {}", id);
        model.addAttribute("recipe", recipeRepository.findWithAllById(id));

        return "recipe/recipe";
    }

    @GetMapping("/register")
    public String save(Model model) {
        return "recipe/register";
    }

    @GetMapping("/modify/{id}")
    public String modifiy(@PathVariable Long id, RecipeSaveForm recipeSaveForm, Model model) {
        log.info("id : {}" , id);
        model.addAttribute("recipe", recipeService.getRecipeForm(id));
        
        return "recipe/modify";
    }


}