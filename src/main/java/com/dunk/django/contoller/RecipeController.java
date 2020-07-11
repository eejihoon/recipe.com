package com.dunk.django.contoller;

import com.dunk.django.dto.PageDTO;
import com.dunk.django.service.RecipeService;

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
    
    //git test
    //이렇게 주석 다는 것으로는 안 되나?
    private final RecipeService service;

    @GetMapping("/list")
    public void getList(@ModelAttribute("pageDTO")PageDTO pageDTO, Model model) {
        log.info("=============/list===========");
        log.info(pageDTO);

        pageDTO.setSize(40);

        model.addAttribute("list", service.getList(pageDTO));
        
    }

}