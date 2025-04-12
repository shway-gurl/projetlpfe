package projectPFE1.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectPFE1.entities.Review;
import projectPFE1.entities.UserEntity;
import projectPFE1.services.ReviewInterface;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewInterface reviewInterface;

    //Add Review
    @PostMapping("/addReview")
    public Review addReview(@Valid @RequestBody Review review)
    {
        return reviewInterface.addReview(review);
    }

    //Get Review by id
    @GetMapping("/getReviewById/{id}")
    public Review getReview(@PathVariable Long id)
    {
        return reviewInterface.getReview(id);
    }

    //Update review or rating
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestParam Long customerId,
            @RequestParam int newRating,
            @RequestParam String newReview) {

        // Call the service to update the review
        Review updatedReview = reviewInterface.updateReview(reviewId, customerId, newRating, newReview);

        // Return the updated review as the response
        return ResponseEntity.ok(updatedReview);
    }

    // Delete a review
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId, @RequestParam Long customerId) {
        try {
            // Call service to delete the review
            reviewInterface.deleteReview(reviewId, customerId);
            return ResponseEntity.ok("Review deleted successfully.");
        } catch (IllegalArgumentException e) {
            // Return an error response if customer is not the owner of the review
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get reviews by transporter ID
    @GetMapping("/transporter/{transporterId}")
    public ResponseEntity<List<Review>> getReviewsByTransporter(@PathVariable Long transporterId) {
        List<Review> reviews = reviewInterface.getReviewsByTransporterId(transporterId);
        return ResponseEntity.ok(reviews);
    }

    // Get reviews by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Review>> getReviewsByCustomer(@PathVariable Long customerId) {
        List<Review> reviews = reviewInterface.getReviewsByCustomerId(customerId);
        return ResponseEntity.ok(reviews);
    }

    // Get average rating for a transporter
    @GetMapping("/transporter/{transporterId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long transporterId) {
        double avgRating = reviewInterface.getAverageRatingByTransporterId(transporterId);
        return ResponseEntity.ok(avgRating);
    }



}
