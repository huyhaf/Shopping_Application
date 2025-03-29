package com.example.shopapp.sevices;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws DataNotFoundException;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetail) throws DataNotFoundException;
    void deleteById(Long id);
    List<OrderDetail> findByOrderId(Long orderId);
}
