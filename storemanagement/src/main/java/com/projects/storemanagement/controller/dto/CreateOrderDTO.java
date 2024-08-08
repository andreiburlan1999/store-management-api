package com.projects.storemanagement.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {

    private Long userId;
    private List<OrderProductDTO> products;

}
