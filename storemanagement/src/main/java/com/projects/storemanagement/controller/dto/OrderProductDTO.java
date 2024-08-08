package com.projects.storemanagement.controller.dto;

import lombok.Data;

@Data
public class OrderProductDTO {

    private Long productId;
    private Integer quantity;
    private Double price;

}
