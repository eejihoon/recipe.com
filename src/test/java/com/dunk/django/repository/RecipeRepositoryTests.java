package com.dunk.django.repository;

import java.util.Optional;

import com.dunk.django.domain.Recipe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class RecipeRepositoryTests {

    @Autowired
    private RecipeRepository repository;

    @Test
    public void testInsert() {
        Recipe entity = Recipe.builder()
        .recipe_name("치킨마요")
        .ingr_list("치킨,마요네즈,간장,김,밥")
        .recipe("치킨과 마요네즈를 밥에 쏙쏙")
        .build();

        repository.save(entity);
    }

    @Test
    public void testGet() {
        Optional<Recipe> entity = repository.findById(182L);

        System.out.println(entity);
    }

    @Test
    public void testGetList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC,"rno");

        Page<Recipe> list = repository.findAll(pageable);
    
        list.forEach(row -> System.out.println(row));
    
        // List<Recipe> list = repository.findAll();

        // list.forEach(result -> System.out.println(result));
    }

    @Test
    public void testUpdate() {
        Recipe entity = repository.findById(180L).get();

        entity.setRecipe_name("한솥 치킨 마요");
        entity.setIngr_list("ingr_list");
        entity.setRecipe("recipe");

        

        // repository.save(entity);
    
    

    }

    // @Test
    // public void testUpdateByQuery(){
    //     int count = repository.update(180L, "lololo", "lololo", "lololo");

    //     System.out.println("COUNT: " + count);
    //  }

    @Test
    public void testDelete() {  //파일 업로드랑 연결 되어 있어서 파일 있는 게시물 삭제 안 됨.
        repository.deleteById(182L);
    }

}