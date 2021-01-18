package com.dunk.django.recipe;


import com.dunk.django.domain.Recipe;
import com.dunk.django.recipe.repository.FoodNationRepository;
import com.dunk.django.recipe.repository.FoodTypeRepository;
import com.dunk.django.recipe.repository.IngredientRepository;
import com.dunk.django.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final FoodNationRepository foodNationRepository;
    private final FoodTypeRepository foodTypeRepository;

    @GetMapping("/recipe")
    public String findAll(Long id, Model model) {
        log.info("id : {}", id);
        model.addAttribute("recipe", recipeRepository.findWithAllById(id));

        return "recipe/recipe";
    }

    @GetMapping("/register")
    public String save(Model model) {
        model.addAttribute("foodType", foodTypeRepository.findAll());
        model.addAttribute("foodNation", foodNationRepository.findAll());

        return "recipe/register";
    }

    @GetMapping("/recipe/{id}")
    public String modifiy() {

        return "recipe/modify";
    }


}