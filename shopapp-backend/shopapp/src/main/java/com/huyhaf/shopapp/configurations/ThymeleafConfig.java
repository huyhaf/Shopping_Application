package com.huyhaf.shopapp.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import java.nio.charset.StandardCharsets;

@Configuration
public class ThymeleafConfig {

    @Bean
    public SpringTemplateEngine springTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(emailTemplateResolver());
        return templateEngine;
    }

    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        
        // Thymeleaf sẽ tìm template trong thư mục: /resources/templates/
        templateResolver.setPrefix("/templates/"); 
        
        // Tìm các file có đuôi .html
        templateResolver.setSuffix(".html"); 
        
        // Kiểu template là HTML
        templateResolver.setTemplateMode(TemplateMode.HTML); 
        
        // Dùng UTF-8 để hỗ trợ tiếng Việt
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name()); 
        
        // Không cần cache template (tốt cho development)
        templateResolver.setCacheable(false); 
        
        return templateResolver;
    }
}