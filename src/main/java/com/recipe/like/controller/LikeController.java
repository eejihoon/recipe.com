package com.recipe.like.controller;

import com.recipe.config.security.LoginMember;
import com.recipe.like.repository.LikeQueryRepository;
import com.recipe.like.service.LikeService;
import com.recipe.member.domain.Member;
import com.recipe.member.utils.MemberAdapter;
import com.recipe.recipe.dto.RecipeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class LikeController {
    private final LikeQueryRepository likeQueryRepository;

    @GetMapping("/scrap")
    public String viewScrappedRecipe(@LoginMember Member loginMember,
                                     @PageableDefault(size = 9, value = 9, sort = "id", direction = Sort.Direction.DESC) Pageable page,
                                     Model model) {
        if (Objects.nonNull(loginMember)) {
            Page<RecipeDto> likedRecipe = likeQueryRepository.findLikedRecipe(loginMember, page);
            model.addAttribute("recipes", likedRecipe);
            model.addAttribute("maxPage" , 9);
        }

        return "recipe/scrap";
    }
}
