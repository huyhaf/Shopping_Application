package com.huyhaf.shopapp.responses;

import com.huyhaf.shopapp.models.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long order_id;

    @JsonProperty("product_id")
    private Long product_id;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("color")
    private String color;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .order_id(orderDetail.getId())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .color(orderDetail.getColor())
                .id(orderDetail.getId())
                .product_id(orderDetail.getProduct().getId())
                .price(orderDetail.getPrice())
                .totalMoney(orderDetail.getTotalMoney())
                .build();
    }
}
