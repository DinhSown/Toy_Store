package com.toystore.mapper;

import com.toystore.dto.response.OrderItemResponse;
import com.toystore.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    OrderItemResponse toResponse(OrderItem orderItem);
}
