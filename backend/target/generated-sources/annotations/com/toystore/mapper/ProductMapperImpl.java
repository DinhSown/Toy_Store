package com.toystore.mapper;

import com.toystore.dto.request.ProductRequest;
import com.toystore.dto.response.ProductResponse;
import com.toystore.entity.Category;
import com.toystore.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-29T15:12:58+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
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
        productResponse.id( product.getId() );
        productResponse.name( product.getName() );
        productResponse.slug( product.getSlug() );
        productResponse.description( product.getDescription() );
        productResponse.price( product.getPrice() );
        productResponse.salePrice( product.getSalePrice() );
        productResponse.stock( product.getStock() );
        productResponse.brand( product.getBrand() );
        productResponse.imageUrl( product.getImageUrl() );
        productResponse.isActive( product.getIsActive() );
        productResponse.isFeatured( product.getIsFeatured() );
        productResponse.createdAt( product.getCreatedAt() );
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
        product.name( request.getName() );
        product.description( request.getDescription() );
        product.price( request.getPrice() );
        product.salePrice( request.getSalePrice() );
        product.stock( request.getStock() );
        product.brand( request.getBrand() );
        product.imageUrl( request.getImageUrl() );
        product.isActive( request.getIsActive() );
        product.isFeatured( request.getIsFeatured() );

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
