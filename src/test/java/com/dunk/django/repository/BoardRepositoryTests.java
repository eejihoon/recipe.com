package com.dunk.django.repository;

import com.dunk.django.domain.Board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository repository;

    @Test
    public void testConnect() {
        System.out.println(repository);
    }

    @Test
    public void testInsert() { //게시물 등록
        Board entity = Board.builder()
        .title("BOOT TEST")
        .content("BOOT CONTENT")
        .writer("COCODORI")
        .build();

        repository.save(entity);
    }

    @Test
    public void testGet() {
        Board entity = repository.findById(1122L).get();
        System.out.println(entity);
    }

    @Test
    public void testGetList() {
        Pageable pageable = PageRequest.of(0,10,Sort.Direction.DESC,"bno");
        Page<Board> board = repository.findAll(pageable);
    
        board.forEach(list -> System.out.println(list));
    
    }

    @Test
    public void testUpdate() {
        //수정할 게시물 불러오기
        Board entity = repository.findById(1122L).get();
    
        //내용 수정
        // entity.changeTitle("change메서드랑 setter랑 다른 건");
        // entity.changeContent("이름 뿐.");
        entity.setTitle("setter수정");
        entity.setContent("content");
        
        //수정
        repository.save(entity);
    }

    @Test
    public void testRemove() {
        repository.deleteById(1121L);
    }

    @Test
    public void updateQuery() {
        repository.updateQuery("title"," content", 1122L);
    }

}