package com.toystore.mapper;

import com.toystore.dto.request.CategoryRequest;
import com.toystore.dto.response.CategoryResponse;
import com.toystore.entity.Category;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-25T14:00:05+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponse toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.parentId( categoryParentId( category ) );
        categoryResponse.parentName( categoryParentName( category ) );
        categoryResponse.children( categoryListToCategoryResponseList( category.getChildren() ) );
        categoryResponse.createdAt( category.getCreatedAt() );
        categoryResponse.description( category.getDescription() );
        categoryResponse.id( category.getId() );
        categoryResponse.imageUrl( category.getImageUrl() );
        categoryResponse.isActive( category.getIsActive() );
        categoryResponse.name( category.getName() );
        categoryResponse.slug( category.getSlug() );
        categoryResponse.updatedAt( category.getUpdatedAt() );

        return categoryResponse.build();
    }

    @Override
    public Category toEntity(CategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.description( request.getDescription() );
        category.imageUrl( request.getImageUrl() );
        category.isActive( request.getIsActive() );
        category.name( request.getName() );

        return category.build();
    }

    private Long categoryParentId(Category category) {
        if ( category == null ) {
            return null;
        }
        Category parent = category.getParent();
        if ( parent == null ) {
            return null;
        }
        Long id = parent.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String categoryParentName(Category category) {
        if ( category == null ) {
            return null;
        }
        Category parent = category.getParent();
        if ( parent == null ) {
            return null;
        }
        String name = parent.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    protected List<CategoryResponse> categoryListToCategoryResponseList(List<Category> list) {
        if ( list == null ) {
            return null;
        }

        List<CategoryResponse> list1 = new ArrayList<CategoryResponse>( list.size() );
        for ( Category category : list ) {
            list1.add( toResponse( category ) );
        }

        return list1;
    }
}
