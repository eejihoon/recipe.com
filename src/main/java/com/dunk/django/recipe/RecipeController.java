package com.dunk.django.recipe;

import com.dunk.django.member.MemberAdapter;
import com.dunk.django.recipe.utils.AuthorVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final AuthorVerification authorVerification;

    @GetMapping("/")
    public String index(@PageableDefault(size = 9, value = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        String keyword,
                        Model model,
                        @AuthenticationPrincipal MemberAdapter memberAdapter) {
        log.info("keyword       : {} ", keyword);
        log.info("pageable      : {}", pageable);
        log.info("memberAdapter : {} ", memberAdapter);

        /*
        *   이메일 인증 안된 사용자에게 알림 띄워주기 위함
        * */
        if (Objects.nonNull(memberAdapter))
            model.addAttribute("member", memberAdapter.getMember());

        model.addAttribute("recipes",
                recipeQueryRepository.findAllRecipeAndSearchWithPaging(keyword, pageable));
        model.addAttribute("maxPage" , 9);

        return "index";
    }

    @GetMapping("/recipe")
    public String findRecipe(Long id,
                          @AuthenticationPrincipal MemberAdapter memberAdapter,
                          Model model) {
        log.info("id : {}", id);

        model.addAttribute("recipe", recipeService.findRecipe(id, memberAdapter));

        return "recipe/recipe";
    }

    @GetMapping("/register")
    public String save(@AuthenticationPrincipal MemberAdapter memberAdapter, Model model) {
        if (Objects.nonNull(memberAdapter)) {
            model.addAttribute("member", memberAdapter.getMember());
        }

        return "recipe/register";
    }

    @GetMapping("/modify/{id}")
    public String modifiy(@PathVariable Long id,
                          @AuthenticationPrincipal MemberAdapter memberAdapter,
                          Model model) throws AccessDeniedException {
        log.info("id : {}" , id);

        RecipeSaveForm recipeForm = recipeService.getRecipeForm(id, memberAdapter.getMember());

        model.addAttribute("recipe", recipeForm);
        model.addAttribute("member", memberAdapter.getMember());

        return "recipe/modify";
    }
}