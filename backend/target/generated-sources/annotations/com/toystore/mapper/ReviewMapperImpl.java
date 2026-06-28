package com.toystore.mapper;

import com.toystore.dto.request.ReviewRequest;
import com.toystore.dto.response.ReviewResponse;
import com.toystore.entity.Product;
import com.toystore.entity.Review;
import com.toystore.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-25T13:53:38+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResponse toResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponse.ReviewResponseBuilder reviewResponse = ReviewResponse.builder();

        reviewResponse.userId( reviewUserId( review ) );
        reviewResponse.userFullName( reviewUserFullName( review ) );
        reviewResponse.userAvatarUrl( reviewUserAvatarUrl( review ) );
        reviewResponse.productId( reviewProductId( review ) );
        reviewResponse.productName( reviewProductName( review ) );
        reviewResponse.id( review.getId() );
        reviewResponse.rating( review.getRating() );
        reviewResponse.comment( review.getComment() );
        reviewResponse.createdAt( review.getCreatedAt() );
        reviewResponse.updatedAt( review.getUpdatedAt() );

        return reviewResponse.build();
    }

    @Override
    public Review toEntity(ReviewRequest request) {
        if ( request == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.rating( request.getRating() );
        review.comment( request.getComment() );

        return review.build();
    }

    private Long reviewUserId(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String reviewUserFullName(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        String fullName = user.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }

    private String reviewUserAvatarUrl(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        String avatarUrl = user.getAvatarUrl();
        if ( avatarUrl == null ) {
            return null;
        }
        return avatarUrl;
    }

    private Long reviewProductId(Review review) {
        if ( review == null ) {
            return null;
        }
        Product product = review.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String reviewProductName(Review review) {
        if ( review == null ) {
            return null;
        }
        Product product = review.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
