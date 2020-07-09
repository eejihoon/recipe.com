package com.dunk.django.service;

import javax.transaction.Transactional;

import com.dunk.django.domain.Preferences;
import com.dunk.django.repository.PreferenceRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class PreferencesServiceImpl implements PreferencesService {

    private final PreferenceRepository repository;

    @Override
    public void getAndRegisterOrModify(Long userId, Long itemId) {

        log.info("userId : " + userId);
        log.info("itemId : " + itemId);

        Preferences entity = repository.findByUserIdAndItemId(userId, itemId);
        log.info("entity : " + entity);

        if(entity == null) {
            entity = Preferences.builder()
            .userId(userId)
            .itemId(itemId)
            .preference(0.5f)
            .build();

            repository.save(entity);
        } else {
            log.info(entity.getPreference());
            float preference = entity.getPreference()+0.5f;
            log.info(preference);
            repository.update(preference, userId, itemId);
        }
    }
    
}