package com.toystore.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toystore.dto.request.ProductRequest;
import com.toystore.dto.response.ProductResponse;
import com.toystore.entity.Product;
import com.toystore.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    public ObjectMapper objectMapper;

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "images", source = "images", qualifiedByName = "jsonToList")
    @Mapping(target = "effectivePrice", expression = "java(product.getEffectivePrice())")
    @Mapping(target = "averageRating", source = "product", qualifiedByName = "calculateAverageRating")
    @Mapping(target = "reviewCount", source = "product", qualifiedByName = "calculateReviewCount")
    public abstract ProductResponse toResponse(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "images", source = "images", qualifiedByName = "listToJson")
    public abstract Product toEntity(ProductRequest request);

    @Named("jsonToList")
    public List<String> jsonToList(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    @Named("listToJson")
    public String listToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    @Named("calculateAverageRating")
    protected Double calculateAverageRating(Product product) {
        if (product.getReviews() == null || product.getReviews().isEmpty()) {
            return 0.0;
        }
        double sum = product.getReviews().stream()
                .mapToInt(Review::getRating)
                .sum();
        double avg = sum / product.getReviews().size();
        return Math.round(avg * 10.0) / 10.0; // Làm tròn 1 chữ số thập phân
    }

    @Named("calculateReviewCount")
    protected Integer calculateReviewCount(Product product) {
        return product.getReviews() != null ? product.getReviews().size() : 0;
    }
}
