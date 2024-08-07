package com.projects.storemanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product")
    @SequenceGenerator(name = "seq_product", sequenceName = "seq_product", allocationSize = 1)
    private Long id;

    private String name;

    private Double price;

    private Integer quantity;

    private String status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
