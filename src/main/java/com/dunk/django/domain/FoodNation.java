package com.dunk.django.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class FoodNation {
    @Id
    private Long id;

    @Column(nullable = false)
    private String nation;

    public FoodNation(String nation) {
        this.nation = nation;
    }
}
