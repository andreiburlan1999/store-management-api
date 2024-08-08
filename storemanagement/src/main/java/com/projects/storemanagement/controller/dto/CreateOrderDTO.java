package com.projects.storemanagement.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {

    private Long customerId;
    private List<OrderProductDTO> products;

}
