package com.dunk.django.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "taste_preferences")
@Getter
@Setter
// @ToString(exclude = {"mno,recipe"})
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "userId")
    // private DjangoMember userId;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "itemId")
    // private Recipe itemId;

    private Long userId;
    private Long itemId;
    private float preference;

}