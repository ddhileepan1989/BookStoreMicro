package com.micro.order.service;

import brave.Span;
import brave.Tracer;
import com.micro.order.dto.InventoryResponse;
import com.micro.order.dto.OrderLineItemsDTO;
import com.micro.order.dto.OrderRequest;
import com.micro.order.model.OrderEntity;
import com.micro.order.model.OrderLineItem;
import com.micro.order.event.OrderPlacedEvent;
import com.micro.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        OrderEntity order = new OrderEntity();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItem> list = orderRequest.getOrderLineItemsDTOS().stream().map(this::mapToEntity).toList();
        order.setOrderLineItems(list);

        List<String> skuList = list.stream().map(OrderLineItem::getSkuCode).toList();

        //Call Inventory
        //stock checking
        Span inventoryServiceLookUp = tracer.nextSpan().name("InventoryServiceLookUp");
        try (Tracer.SpanInScope spanInScope = tracer.withSpanInScope(inventoryServiceLookUp.start())) {
            InventoryResponse[] skuCodeList = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuList).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();


            boolean result = Arrays.stream(skuCodeList).allMatch(InventoryResponse::isInStock);

            if (!CollectionUtils.isEmpty(List.of(skuCodeList)) && result) {
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic1",new OrderPlacedEvent(order.getOrderNumber()));
                return "Order Placed Successfully ! "+order.getOrderNumber();
            }

            throw new IllegalArgumentException("Product is not in Stock, Please try again later");
        } finally {
            inventoryServiceLookUp.finish();
        }

    }

    private OrderLineItem mapToEntity(OrderLineItemsDTO orderLineItemsDTO) {
        return OrderLineItem.builder()
                .skuCode(orderLineItemsDTO.getSkuCode())
                .price(orderLineItemsDTO.getPrice())
                .quantity(orderLineItemsDTO.getQuantity())
                .build();
    }
}
