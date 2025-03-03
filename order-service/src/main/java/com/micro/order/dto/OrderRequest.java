package com.micro.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderRequest {
    private List<OrderLineItemsDTO> orderLineItemsDTOS;
}
