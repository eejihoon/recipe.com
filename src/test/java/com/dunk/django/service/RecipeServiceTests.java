package com.dunk.django.service;

import java.util.stream.IntStream;

import com.dunk.django.dto.PageDTO;
import com.dunk.django.dto.RecipeDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RecipeServiceTests {
    
    @Autowired
    private RecipeService service;

    @Test
    public void testRegister() {
        
        IntStream.rangeClosed(1, 10).forEach(i -> {
            RecipeDTO dto = RecipeDTO.builder()
            .recipe_name("새로운 레시피.."+i)
            .ingr_list("여러 재료 등등등.."+i)
            .recipe("레시피 주소..."+i)
            .build();

            System.out.println(service.register(dto));
        });
    }

    @Test
    public void testGet() {
        System.out.println(service.get(192L));
    }

    @Test
    public void testGetList() {
        PageDTO pageDTO = new PageDTO();

        System.out.println(service.getList(pageDTO));
    }

    @Test
    public void testModify() {  //select 2번 날라간다.
        RecipeDTO dto = service.bindToDTO(service.get(186L)); 
        
        dto.setRecipe_name("modify");
        dto.setIngr_list("modify....");
        dto.setRecipe(".....");
    
        System.out.println(service.modify(dto));
    }

    @Test
    public void testRemove() {
        service.remove(185L);
    }



}