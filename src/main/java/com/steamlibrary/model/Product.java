package com.steamlibrary.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(0)
    private Double price;

    // 0 means no discount, must be between 0-100
    @Column(nullable = false)
    @Min(0)
    @Max(100)
    private int discountPercent = 0;

    private String headerImageUrl;
    private String capsuleImageUrl;

    // New media fields for enhanced game detail page
    @Column(name = "screenshot1url")
    private String screenshot1Url;
    @Column(name = "screenshot2url")
    private String screenshot2Url;
    @Column(name = "screenshot3url")
    private String screenshot3Url;
    @Column(name = "screenshot4url")
    private String screenshot4Url;
    @Column(name = "screenshot5url")
    private String screenshot5Url;

    private String trailerVideoUrl; // WebM video URL
    private String backgroundImageUrl; // Large background image

    @Column(length = 1000)
    private String aboutTheGame; // Longer "About" section

    // e.g. "Action, RPG, Open World"
    private String tags;

    // e.g. "Mostly Positive", "Very Positive", "Overwhelmingly Positive"
    private String reviewSummary;

    private String developer;
    private String publisher;

    private Boolean featured = false;
}
