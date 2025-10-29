package com.huyhaf.shopapp.sevices;

import com.huyhaf.shopapp.dtos.OrderDTO;
import com.huyhaf.shopapp.exceptions.DataNotFoundException;
import com.huyhaf.shopapp.models.Order;
import com.huyhaf.shopapp.models.OrderStatus;
import com.huyhaf.shopapp.models.User;
import com.huyhaf.shopapp.repositories.OrderRepository;
import com.huyhaf.shopapp.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate <String,Long> kafkaTemplate;

    private static final String ORDER_CREATED_TOPIC= "order_created";
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);


    @Override
    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));
        //convert orderDTO to Order
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        // check shipping date must be after today
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least to day");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);

        try {
            kafkaTemplate.send(ORDER_CREATED_TOPIC, order.getId());
            logger.info("Sent order created event to Kafka for order id: " + order.getId());
        } catch (Exception e) {
            logger.error("Failed to send order created event to Kafka for order id: " + order.getId(), e);
        }

        return order;
    }

    @Override
    @Transactional
    @CachePut(value = "orders", key = "#id")
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Can not find order with id: "+ id));
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() ->
            new DataNotFoundException("Can not find user with id: " + orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#id")
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    @Cacheable(value = "ordersByUserId", key = "#userId")
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
