package com.huyhaf.shopapp.sevices;

import com.huyhaf.shopapp.dtos.OrderDTO;
import com.huyhaf.shopapp.exceptions.DataNotFoundException;
import com.huyhaf.shopapp.models.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    Order getOrder(Long id);
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
}