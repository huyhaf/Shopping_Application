package com.huyhaf.shopapp.sevices;

import com.huyhaf.shopapp.dtos.OrderDetailDTO;
import com.huyhaf.shopapp.exceptions.DataNotFoundException;
import com.huyhaf.shopapp.models.Order;
import com.huyhaf.shopapp.models.OrderDetail;
import com.huyhaf.shopapp.models.Product;
import com.huyhaf.shopapp.repositories.OrderDetailRepository;
import com.huyhaf.shopapp.repositories.OrderRepository;
import com.huyhaf.shopapp.repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    @CacheEvict(value = "orderDetails", allEntries = true)
    public OrderDetail createOrderDetail(OrderDetailDTO newOrderDetailDTO) throws DataNotFoundException {
        //check whether the order exists
        Order order = orderRepository.findById(newOrderDetailDTO.getOrderId()).orElseThrow(() ->
                new DataNotFoundException("Can not find order with id: "+ newOrderDetailDTO.getOrderId()));
        // check whether the product exists
        Product product = productRepository.findById(newOrderDetailDTO.getProductId()).orElseThrow(() ->
                new DataNotFoundException("Can not find product with id: "+ newOrderDetailDTO.getProductId()));
        OrderDetail newOrderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(newOrderDetailDTO.getPrice())
                .color(newOrderDetailDTO.getColor())
                .totalMoney(newOrderDetailDTO.getTotalMoney())
                .numberOfProducts(newOrderDetailDTO.getNumberOfProducts())
                .build();
        return orderDetailRepository.save(newOrderDetail);
    }

    @Override
    @Cacheable(value = "orderDetails", key = "#id")
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Can not find orderdetail with id "+ id));
    }

    @Override
    @Transactional
    @CachePut(value = "orderDetails", key = "#id")
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailDTO) throws DataNotFoundException {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Can not find orderdetail with id "+ id));
        Order existingOrder = orderRepository.findById(newOrderDetailDTO.getOrderId()).orElseThrow(() ->
                new DataNotFoundException("Can not find order with id "+ newOrderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(newOrderDetailDTO.getProductId()).orElseThrow(() ->
                new DataNotFoundException("Can not find product with id "+ newOrderDetailDTO.getProductId()));
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        existingOrderDetail.setColor(newOrderDetailDTO.getColor());
        existingOrderDetail.setTotalMoney(newOrderDetailDTO.getTotalMoney());
        existingOrderDetail.setNumberOfProducts(newOrderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setPrice(newOrderDetailDTO.getPrice());
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    @Transactional
    @CacheEvict(value = "orderDetails", key = "#id")
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "orderDetailsByOrderId", key = "#orderId")
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
