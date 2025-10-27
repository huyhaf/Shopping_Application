package com.huyhaf.shopapp.sevices;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.huyhaf.shopapp.exceptions.DataNotFoundException;
import com.huyhaf.shopapp.models.Order;
import com.huyhaf.shopapp.models.OrderDetail;
import com.huyhaf.shopapp.repositories.OrderDetailRepository;
import com.huyhaf.shopapp.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessingService.class);

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final EmailService emailService;

    /**
     * Lắng nghe topic 'order_created'.
     * groupId phải khớp với cấu hình trong application.yml
     */
    @KafkaListener(topics = "order_created", groupId = "shopapp-group")
    public void handleOrderCreated(Long orderId) {
        try {
            logger.info("Received order created event for orderId: {}", orderId);

            // 1. Lấy thông tin đơn hàng đầy đủ từ DB
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new DataNotFoundException("Order not found in Kafka consumer"));
            // 2. Lấy danh sách chi tiết đơn hàng
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
            // // 2. [TÁC VỤ CHẬM 1] Gửi email xác nhận
            // // Giả lập việc gửi email (tốn 2 giây)
            // logger.info("Simulating sending confirmation email for order {} to {}", order.getId(), order.getEmail());
            // Thread.sleep(2000); 
            emailService.sendOrderConfirmationEmail(order,orderDetails);
            logger.info("Sent confirmation email for order {}", orderId);

            // 3. [TÁC VỤ CHẬM 2] Cập nhật hệ thống kho bãi
            // Giả lập việc gọi API kho (tốn 1 giây)
            logger.info("Simulating updating inventory for order {}...", order.getId());
            Thread.sleep(1000);
            
            // 4. [TÁC VỤ CHẬM 3] Thông báo cho Admin (qua Slack, Telegram...)
            logger.info("Simulating notifying admin about new order {}", order.getId());
            Thread.sleep(500);

            logger.info("Successfully processed order {}", orderId);

        } catch (Exception e) {
            // Nếu xử lý thất bại, Kafka sẽ tự động thử lại (retry) message này
            logger.error("Error processing order {}: {}", orderId, e.getMessage());
            // Cần cấu hình Dead Letter Queue (DLQ) để xử lý các lỗi vĩnh viễn
        }
    }
}
