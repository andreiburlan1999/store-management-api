package com.projects.storemanagement.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {

    private Long customerId;
    private List<OrderProductDto> products;

}
