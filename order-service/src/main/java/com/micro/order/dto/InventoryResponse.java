package com.micro.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class InventoryResponse {

    private String skuCode;
    private boolean isInStock;
}
