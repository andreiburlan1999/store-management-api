package com.projects.storemanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_product")
public class OrderProduct {

    @EmbeddedId
    private OrderProductPk id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @Lazy
    @JsonIgnore
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Double price;

}
