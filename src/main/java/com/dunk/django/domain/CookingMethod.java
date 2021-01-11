package com.dunk.django.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class CookingMethod {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Recipe recipe;

    private int sequence;

    private String description;

    private String image;
}
