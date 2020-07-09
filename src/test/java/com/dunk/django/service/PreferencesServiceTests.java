package com.dunk.django.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PreferencesServiceTests {
    
    @Autowired
    PreferencesService service;

    @Test
    public void testGetAndRegisterORModify() {
        service.getAndRegisterOrModify(15L, 286L);
    }
    
/*
    @Test
    public void testGet() {
        System.out.println(service.get(1L,6L));
    }

    @Test
    public void testRegister() {
        PreferencesDTO dto = PreferencesDTO.builder()
        .userId(1L)
        .itemId(7L)
        .preference(0.5f)
        .build();

        service.register(dto);
    }

    @Test
    public void testUpdate() {
        
        PreferencesDTO dto = service.get(1L, 7L);
        
        dto.setPreference(dto.getPreference()+0.5f);

        service.update(dto);
    }

    */
}