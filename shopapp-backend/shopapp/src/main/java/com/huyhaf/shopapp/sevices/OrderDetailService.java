package com.huyhaf.shopapp.sevices;

import com.huyhaf.shopapp.dtos.OrderDetailDTO;
import com.huyhaf.shopapp.exceptions.DataNotFoundException;
import com.huyhaf.shopapp.models.Order;
import com.huyhaf.shopapp.models.OrderDetail;
import com.huyhaf.shopapp.models.Product;
import com.huyhaf.shopapp.repositories.OrderDetailRepository;
import com.huyhaf.shopapp.repositories.OrderRepository;
import com.huyhaf.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
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
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Can not find orderdetail with id "+ id));
    }

    @Override
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
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
