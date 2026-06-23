package com.steamlibrary.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Double price;

    // 0 means no discount
    private Integer discountPercent = 0;

    private String headerImageUrl;
    private String capsuleImageUrl;

    // e.g. "Action, RPG, Open World"
    private String tags;

    // e.g. "Mostly Positive", "Very Positive", "Overwhelmingly Positive"
    private String reviewSummary;

    private String developer;
    private String publisher;

    private Boolean featured = false;
}
