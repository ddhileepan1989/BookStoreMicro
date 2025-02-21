package com.micro.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderLineItemsDTO {
    private Long id;
    private String skuCode;
    private Integer quantity;
    private BigDecimal price;
}
