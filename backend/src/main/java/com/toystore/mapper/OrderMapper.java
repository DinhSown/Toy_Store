package com.toystore.mapper;

import com.toystore.dto.response.OrderResponse;
import com.toystore.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFullName", source = "user.fullName")
    OrderResponse toResponse(Order order);
}
