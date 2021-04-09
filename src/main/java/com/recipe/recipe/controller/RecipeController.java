package com.recipe.recipe.controller;

import com.recipe.config.security.LoginMember;
import com.recipe.member.domain.Member;
import com.recipe.recipe.dto.RecipeDto;
import com.recipe.recipe.repository.RecipeQueryRepository;
import com.recipe.recipe.dto.RecipeSaveForm;
import com.recipe.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeQueryRepository recipeQueryRepository;

    @GetMapping("/")
    public String index(@PageableDefault(size = 9, value = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        String keyword,
                        Model model,
                        @LoginMember Member loginMember) {
        log.info("keyword       : {} ", keyword);
        log.info("pageable      : {}", pageable);
        log.info("memberAdapter : {} ", loginMember);

        /*
         *   이메일 인증 안된 사용자에게 알림 띄워주기 위함
         * */
        if (Objects.nonNull(loginMember))
            model.addAttribute("member", loginMember);

        model.addAttribute("recipes",
                recipeQueryRepository.findAllRecipeAndSearchWithPaging(keyword, pageable));
        model.addAttribute("maxPage", 9);

        return "index";
    }

    @GetMapping("/recipe")
    public String findRecipe(Long id,
                             @LoginMember Member loginMember,
                             Model model) throws AccessDeniedException {
        log.info("id : {}", id);

        model.addAttribute("recipe", recipeService.findRecipe(id, loginMember));

        return "recipe/recipe";
    }

    @GetMapping("/register")
    public String save(@LoginMember Member loginMember, Model model) {
        model.addAttribute("member", loginMember);
        return "recipe/register";
    }

    @GetMapping("/modify/{id}")
    public String modifiy(@PathVariable Long id,
                          @LoginMember Member loginMember,
                          Model model) throws AccessDeniedException {
        log.info("id : {}", id);

        RecipeSaveForm recipeForm = recipeService.getRecipeModifyForm(id, loginMember);

        model.addAttribute("recipe", recipeForm);
        model.addAttribute("member", loginMember);

        return "recipe/modify";
    }

    //내가 쓴 게시물
    @GetMapping("/myrecipe")
    public String mypost(@LoginMember Member loginMember,
                         @PageableDefault(size = 9, value = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                         Model model) {
        log.info("memberAdapter : {} ", loginMember);

        if (Objects.nonNull(loginMember)) {
            Page<RecipeDto> byMember = recipeQueryRepository.findByMember(loginMember, pageable);
            model.addAttribute("recipes", byMember);
            model.addAttribute("maxPage", 9);
        }

        return "recipe/my-recipe";
    }
}