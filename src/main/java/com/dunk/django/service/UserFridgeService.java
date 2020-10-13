package com.dunk.django.service;

import java.util.List;

import javax.transaction.Transactional;

import com.dunk.django.domain.UserFridge;
import com.dunk.django.dto.UserFridgeDTO;

@Transactional
public interface UserFridgeService {
    Long register(UserFridgeDTO dto);

    // --추가 ---
    // 사용자를 하나 가져와서
    // 그 사용자가 가진 냉장고리스트를 뽑아낸다.
    List<UserFridge> get(String userid);

    void remove(Long fno); // 선택삭제시

    void removeUsername(String username); // 전체삭제시

    // 식재료가 들어간 태그를 찾아서 그 태그의 cno를 찾고
    // 그 cno를 가지고 insert하는 메서드
    // DTO로 받아서 구현계층에서 변환을 하고
    // 변환한 데이터를 매개변수로 사용 해 이 메서드를 호출한다.
    Long scanAndRegist(UserFridgeDTO dto);

    // 재료이름를 매개변수로 받아 태그에 재료이름이 들어간 식재료번호를 가져온다.
    Long getCategoryCno(String ingr_name);

    // 식재료를 넣어서 카테고리 가져오기.
    String searchCategory(String ingrName);

    // -------------------
    default UserFridge bindToEntity(UserFridgeDTO dto) {

        UserFridge entity = UserFridge.builder().fno(dto.getFno()).username(dto.getUsername())
                .ingr_name(dto.getIngr_name()).cno(dto.getCno()).build();

        return entity;
    }

    default UserFridgeDTO bindToDTO(UserFridge entity) {

        UserFridgeDTO dto = UserFridgeDTO.builder().fno(entity.getFno()).username(entity.getUsername())
                .ingr_name(entity.getIngr_name()).cno(entity.getCno()).build();

        return dto;

    }
}