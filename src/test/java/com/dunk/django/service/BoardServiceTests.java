package com.dunk.django.service;

import com.dunk.django.dto.BoardDTO;
import com.dunk.django.dto.PageDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class BoardServiceTests {
    
    @Autowired
    BoardService service;

    @Test
    public void testRegister() {
        BoardDTO dto = BoardDTO.builder()
        .title("service test")
        .content("hello")
        .writer("admin")
        .build();

        service.register(dto);
    }

    @Test
    public void testGet() {
        System.out.println(service.get(1122L));
    }

    // @Test
    // public void testGetList() {
    //     Pageable pageable = PageRequest.of(0,10,Sort.Direction.DESC,"bno");
    //     System.out.println(service.getList(pageable));
    // }

    @Test
    public void testModify() {
        
        BoardDTO dto = service.bindToDTO(service.get(1128L));

        service.modify(dto);
    }

    @Test
    public void testRemove() {
        service.remove(1120L);
    }

    @Test
    public void testPaging() {
        
        PageDTO pageDTO = new PageDTO();

        System.out.println(service.getList(pageDTO)); 

    }
}