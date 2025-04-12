package projectPFE1.services;

import projectPFE1.entities.Review;
import projectPFE1.entities.UserEntity;

import java.util.List;

public interface ReviewInterface {
    Review addReview(Review review);
    Review getReview(Long id);
    public Review updateReview(Long reviewId, Long customerId, int newRating, String newReview);
    public void deleteReview(Long reviewId, Long customerId);
    List<Review> getReviewsByTransporterId(Long transporterId);
    List<Review> getReviewsByCustomerId(Long customerId);
    double getAverageRatingByTransporterId(Long transporterId);
}
