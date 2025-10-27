package com.huyhaf.shopapp.sevices;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.huyhaf.shopapp.models.Order;
import com.huyhaf.shopapp.models.OrderDetail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendOrderConfirmationEmail(Order order,List<OrderDetail> orderDetails) {
        try {
            Context context = new Context();
            context.setVariable("order", order);
            context.setVariable("orderDetails", orderDetails);
            String htmlContent = templateEngine.process("order-confirmation", context);

            // 5. Tạo MimeMessage (dùng cho HTML, file đính kèm...)
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            
            // 6. Dùng MimeMessageHelper để thiết lập
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            
            helper.setFrom("no-reply@shopapp.com"); // Email gửi
            helper.setTo(order.getEmail()); // Email nhận
            helper.setSubject("Xác nhận đơn hàng #" + order.getId()); // Tiêu đề
            
            // 7. Set nội dung là HTML (tham số thứ 2 = true)
            helper.setText(htmlContent, true); 

            // 8. Gửi mail
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            // Xử lý lỗi (ví dụ: ghi log) nếu gửi mail thất bại
            e.printStackTrace();
        }
    }
}
