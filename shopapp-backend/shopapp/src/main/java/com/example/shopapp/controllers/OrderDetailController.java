package com.example.shopapp.controllers;

import com.example.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO
    ){
        return ResponseEntity.ok("Order Detail: "+orderDetailDTO+ " created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") Long id
    ){
        return ResponseEntity.ok("Get Order Detail with id " + id);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(
            @Valid @PathVariable("orderId") Long orderId
    ){
        return ResponseEntity.ok("Get Order Details with orderId: " + orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetails(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO newOrderDetailData
    ){
        return ResponseEntity.ok("Update Order Details with id: " + id+" newOrderDetailData: "+ newOrderDetailData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(
            @Valid @PathVariable("id") Long id
    ){
        return ResponseEntity.noContent().build();
    }
}
