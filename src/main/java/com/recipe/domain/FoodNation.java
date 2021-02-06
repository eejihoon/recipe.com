package com.recipe.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class FoodNation {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String nation;

    public FoodNation(String nation) {
        this.nation = nation;
    }
}
