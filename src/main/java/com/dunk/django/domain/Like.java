package com.dunk.django.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor @Getter @Setter @AllArgsConstructor
@Table(name = "likes")
@Entity
public class Like {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Like(Recipe recipe, Member member) {
        this.recipe = recipe;
        this.member = member;
    }
}
