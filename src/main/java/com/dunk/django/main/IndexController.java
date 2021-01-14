package com.dunk.django.main;

import com.dunk.django.recipe.repository.RecipeRepository;
import com.dunk.django.userfridge.UserFridgeService;

import com.dunk.django.util.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class IndexController {

    private final UserFridgeService fridgeService;
    private final RecipeRepository recipeRepository;

    @GetMapping("/")
    public String index(Page page, @PageableDefault(size = 9, value = 9) Pageable pageable, Model model) {

        model.addAttribute("recipes", recipeRepository.findAll(pageable));
        model.addAttribute("maxPage" , 9);
        return "index";
    }

    // ------------------추가-------------------
    @GetMapping("/myFridge")
    public void getMyFridge(Authentication auth, Model model) {
        log.info("================get My Fridge==============");
        log.info("auth : " + auth);
        // 로그인 안했을 때 null
        if (auth == null) {
            log.info("auth null");
            return;
        }
        // 사용자 한명의 냉장고 내용출력
        log.info("getList : " + fridgeService.get(auth.getName()));
        model.addAttribute("fridgeList", fridgeService.get(auth.getName()));
        model.addAttribute("username", auth.getName());
    }

    // 냉장고 전체삭제
    @PostMapping("/removeFridge")
    public String removeFridge(Authentication auth, RedirectAttributes rttr) {
        log.info("************************FRIDGE REMOVE*****************************");
        log.info("id : " + auth.getName());
        // auth.getName()이 유저의 아이디.
        fridgeService.removeUsername(auth.getName());
        rttr.addAttribute("username", auth.getName());

        return "redirect:/django/myFridge";
    }

    // 냉장고 선택삭제하기.
    @DeleteMapping(value = "/{fno}", produces = { MediaType.TEXT_PLAIN_VALUE })
    @ResponseBody
    public ResponseEntity<String> selectRemoveFridge(@PathVariable("fno") Long fno) {
        fridgeService.remove(fno);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @PostMapping("/crawling")
    public void postTest() {
        log.info("==========================================================================");
        log.info("POST CRAWLING ============================================================");
    }

}