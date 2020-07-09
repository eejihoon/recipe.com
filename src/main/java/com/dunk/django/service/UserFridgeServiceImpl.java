package com.dunk.django.service;

import com.dunk.django.domain.UserFridge;
import com.dunk.django.dto.UserFridgeDTO;
import com.dunk.django.repository.UserFridgeRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFridgeServiceImpl implements UserFridgeService {

    private final UserFridgeRepository repository;

    @Override
    public Long register(UserFridgeDTO dto) {

        UserFridge entity = bindToEntity(dto); 

        repository.save(entity);

        return entity.getFno();
    }
    
}