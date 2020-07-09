package com.dunk.django.contoller;

import com.dunk.django.domain.Recipe;
import com.dunk.django.dto.GenericListDTO;
import com.dunk.django.dto.PageDTO;
import com.dunk.django.dto.RecipeDTO;
import com.dunk.django.service.MemberService;
import com.dunk.django.service.PreferencesService;
import com.dunk.django.service.RecipeService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("/django")
@Log4j2
public class IndexController {
    
    private final RecipeService service;  
    private final PreferencesService preferencesService;
    private final MemberService memberService;

    @GetMapping("/index")
    public void index(@ModelAttribute("pageDTO")PageDTO pageDTO, Model model) {
        log.info("===============================INDEX==================================");
        GenericListDTO<RecipeDTO, Recipe> result =
        service.getList(pageDTO);

        result.getDtoList().forEach(dto -> log.info(dto));

        model.addAttribute("list", result);
    }

    @GetMapping("/scan")
    public void scan() {
        log.info("===============================SCAN==================================");
    }

    @GetMapping("/recipe")
    public void recipeGet(@RequestParam("itemId") Long itemId, Authentication auth,Model model) {
        log.info("=======================get==================");
        log.info("itemId : " + itemId);
        // log.info("id : " + auth.getName());
 
        model.addAttribute("get", service.get(itemId));

        //로그인 중일 때만
        if(auth!=null){
            Long userId = memberService.getMno(auth.getName()).get().getUserId();
    
            log.info(userId);
            //userId와 itemId로 select한다.
    
            preferencesService.getAndRegisterOrModify(userId, itemId);
            
        }
    }
}