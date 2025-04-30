package com.example.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Double basePrice;
    private Double salePrice;
    private Double costPrice;
    private String currency;
    private Double taxRate;
}
