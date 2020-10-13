package com.dunk.django.service;

import java.util.List;

import com.dunk.django.domain.UserFridge;
import com.dunk.django.dto.UserFridgeDTO;
import com.dunk.django.repository.UserFridgeRepository;

import org.springframework.data.domain.Sort;
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

    // --추가--
    // 재료이름 - 태그 비교 + 유통기한 자동설정
    @Override
    public Long scanAndRegist(UserFridgeDTO dto) {

        UserFridge entity = bindToEntity(dto);

        repository.insertAfterCalDate(entity);

        return entity.getFno();
    }

    // 냉장고 페이지 들어갈때사용.
    @Override
    public List<UserFridge> get(String userid) {
        Sort sort = Sort.by("fno").descending();
        return repository.getUserNameAndPage(userid, sort);
    }

    // 선택삭제시 사용
    @Override
    public void remove(Long fno) {
        repository.deleteById(fno);
    }

    // 전체삭제시 사용.
    @Override
    public void removeUsername(String userid) {
        repository.removeUsername(userid);
    }

    // 재료이름과 태그를 비교할때 사용.
    @Override
    public Long getCategoryCno(String ingr_name) {
        // 번호가 추출될것임.
        return repository.getCategoryCno(ingr_name);
    }

    // 태그와 비교한것을 통해 카테고리를 뽑아오기.
    @Override
    public String searchCategory(String ingrName) {
        return repository.searchCategory(ingrName);
    }
}