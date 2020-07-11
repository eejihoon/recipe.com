package com.dunk.django.contoller;

import com.dunk.django.dto.PageDTO;
import com.dunk.django.service.MemberService;
import com.dunk.django.service.PreferencesService;
import com.dunk.django.service.RecipeService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService service;
    private final MemberService memberService;
    private final PreferencesService preferencesService;

    @GetMapping("/list")
    public void getList(@ModelAttribute("pageDTO")PageDTO pageDTO, Model model) {
        log.info("=============/list===========");
        log.info(pageDTO);

        pageDTO.setSize(40);

        model.addAttribute("list", service.getList(pageDTO));
        
    }

    @GetMapping("/get")
    public void recipeGet(Long itemId, @ModelAttribute("pageDTO")PageDTO pageDTO, Authentication auth,Model model) {
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