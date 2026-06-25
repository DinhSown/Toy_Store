package com.toystore.mapper;

import com.toystore.dto.response.OrderItemResponse;
import com.toystore.dto.response.OrderResponse;
import com.toystore.entity.Order;
import com.toystore.entity.OrderItem;
import com.toystore.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-25T14:00:05+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder();

        orderResponse.userId( orderUserId( order ) );
        orderResponse.userFullName( orderUserFullName( order ) );
        orderResponse.createdAt( order.getCreatedAt() );
        orderResponse.id( order.getId() );
        orderResponse.items( orderItemListToOrderItemResponseList( order.getItems() ) );
        orderResponse.note( order.getNote() );
        orderResponse.orderCode( order.getOrderCode() );
        orderResponse.paymentMethod( order.getPaymentMethod() );
        orderResponse.shippingAddress( order.getShippingAddress() );
        orderResponse.shippingName( order.getShippingName() );
        orderResponse.shippingPhone( order.getShippingPhone() );
        orderResponse.status( order.getStatus() );
        orderResponse.totalAmount( order.getTotalAmount() );
        orderResponse.updatedAt( order.getUpdatedAt() );

        return orderResponse.build();
    }

    private Long orderUserId(Order order) {
        if ( order == null ) {
            return null;
        }
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String orderUserFullName(Order order) {
        if ( order == null ) {
            return null;
        }
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        String fullName = user.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }

    protected List<OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemResponse> list1 = new ArrayList<OrderItemResponse>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemMapper.toResponse( orderItem ) );
        }

        return list1;
    }
}
