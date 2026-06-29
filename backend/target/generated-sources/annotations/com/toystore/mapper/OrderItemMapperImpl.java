package com.toystore.mapper;

import com.toystore.dto.response.OrderItemResponse;
import com.toystore.entity.OrderItem;
import com.toystore.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-29T15:20:04+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItemResponse toResponse(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemResponse.OrderItemResponseBuilder orderItemResponse = OrderItemResponse.builder();

        orderItemResponse.productId( orderItemProductId( orderItem ) );
        orderItemResponse.id( orderItem.getId() );
        orderItemResponse.productImage( orderItem.getProductImage() );
        orderItemResponse.productName( orderItem.getProductName() );
        orderItemResponse.quantity( orderItem.getQuantity() );
        orderItemResponse.subtotal( orderItem.getSubtotal() );
        orderItemResponse.unitPrice( orderItem.getUnitPrice() );

        return orderItemResponse.build();
    }

    private Long orderItemProductId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Product product = orderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
