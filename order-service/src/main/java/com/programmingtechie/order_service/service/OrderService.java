package com.programmingtechie.order_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.programmingtechie.order_service.dto.OrderLineItemsDto;
import com.programmingtechie.order_service.dto.OrderRequest;
import com.programmingtechie.order_service.model.Order;
import com.programmingtechie.order_service.model.OrderLineItems;
import com.programmingtechie.order_service.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
    
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoLists()
        .stream()
        .map(this::mapToDto)
        .toList();

        order.setOrderLineItemsList(orderLineItems);
        this.orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineLineItemsDto) {
        
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineLineItemsDto.getPrice());
        orderLineItems.setSkucode(orderLineLineItemsDto.getSkucode());
        orderLineItems.setQuantity(orderLineLineItemsDto.getQuantity());
        return orderLineItems;
    }
}
