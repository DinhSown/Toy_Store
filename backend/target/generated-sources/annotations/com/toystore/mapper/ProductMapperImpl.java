package com.toystore.mapper;

import com.toystore.dto.request.ProductRequest;
import com.toystore.dto.response.ProductResponse;
import com.toystore.entity.Category;
import com.toystore.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-29T15:20:04+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl extends ProductMapper {

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.categoryId( productCategoryId( product ) );
        productResponse.categoryName( productCategoryName( product ) );
        productResponse.images( jsonToList( product.getImages() ) );
        productResponse.averageRating( calculateAverageRating( product ) );
        productResponse.reviewCount( calculateReviewCount( product ) );
        productResponse.brand( product.getBrand() );
        productResponse.createdAt( product.getCreatedAt() );
        productResponse.description( product.getDescription() );
        productResponse.id( product.getId() );
        productResponse.imageUrl( product.getImageUrl() );
        productResponse.isActive( product.getIsActive() );
        productResponse.isFeatured( product.getIsFeatured() );
        productResponse.name( product.getName() );
        productResponse.price( product.getPrice() );
        productResponse.salePrice( product.getSalePrice() );
        productResponse.slug( product.getSlug() );
        productResponse.stock( product.getStock() );
        productResponse.updatedAt( product.getUpdatedAt() );

        productResponse.effectivePrice( product.getEffectivePrice() );

        return productResponse.build();
    }

    @Override
    public Product toEntity(ProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.images( listToJson( request.getImages() ) );
        product.brand( request.getBrand() );
        product.description( request.getDescription() );
        product.imageUrl( request.getImageUrl() );
        product.isActive( request.getIsActive() );
        product.isFeatured( request.getIsFeatured() );
        product.name( request.getName() );
        product.price( request.getPrice() );
        product.salePrice( request.getSalePrice() );
        product.stock( request.getStock() );

        return product.build();
    }

    private Long productCategoryId(Product product) {
        if ( product == null ) {
            return null;
        }
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String productCategoryName(Product product) {
        if ( product == null ) {
            return null;
        }
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
