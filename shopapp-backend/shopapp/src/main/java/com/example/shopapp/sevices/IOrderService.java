package com.example.shopapp.sevices;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    Order getOrder(Long id);
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
}