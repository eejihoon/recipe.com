package com.dunk.django.service;

import javax.transaction.Transactional;

import com.dunk.django.domain.UserFridge;
import com.dunk.django.dto.UserFridgeDTO;

@Transactional
public interface UserFridgeService {
    Long register(UserFridgeDTO dto);

    default UserFridge bindToEntity(UserFridgeDTO dto){

        UserFridge  entity = UserFridge.builder()
        .fno(dto.getFno())
        .username(dto.getUsername())
        .ingr_name(dto.getIngr_name())
        .cno(dto.getCno())
        .build();

        return entity;
    } 

    default UserFridgeDTO bindToDTO(UserFridge entity){

        UserFridgeDTO  dto = UserFridgeDTO.builder()
        .fno(entity.getFno())
        .username(entity.getUsername())
        .ingr_name(entity.getIngr_name())
        .cno(entity.getCno())
        .build();

        return dto;
    
    }
}