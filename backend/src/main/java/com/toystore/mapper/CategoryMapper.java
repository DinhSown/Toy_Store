package com.toystore.mapper;

import com.toystore.dto.request.CategoryRequest;
import com.toystore.dto.response.CategoryResponse;
import com.toystore.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    CategoryResponse toResponse(Category category);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "slug", ignore = true) // Slug tạo từ name qua SlugUtil ở Service
    Category toEntity(CategoryRequest request);
}
