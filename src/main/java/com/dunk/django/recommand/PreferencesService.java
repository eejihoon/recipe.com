package com.dunk.django.recommand;

import com.dunk.django.domain.Preferences;

public interface PreferencesService {
 
    void getAndRegisterOrModify(Long userId, Long itemId);

    default Preferences bindToEntity(PreferencesDTO dto) {
        Preferences entity = Preferences.builder()
        .userId(dto.getUserId())
        .itemId(dto.getItemId())
        .preference(dto.getPreference())
        .build();

        return entity;
    }

    default PreferencesDTO bindToDTO(Preferences entity) {
        PreferencesDTO dto = PreferencesDTO.builder()
        .userId(entity.getUserId())
        .itemId(entity.getItemId())
        .preference(entity.getPreference())
        .build();

        return dto;
    }
}