package com.projects.storemanagement.controller.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private String name;
    private Double price;
    private Integer quantity;
    private String status;
    private Long categoryId;

}
