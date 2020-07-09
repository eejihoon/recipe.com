package com.dunk.django.repository;

import com.dunk.django.domain.UserFridge;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFridgeRepository extends JpaRepository<UserFridge,Long>{
    
}