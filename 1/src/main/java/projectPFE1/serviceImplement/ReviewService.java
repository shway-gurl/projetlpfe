package projectPFE1.serviceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectPFE1.entities.Review;
import projectPFE1.entities.RoleName;
import projectPFE1.entities.UserEntity;
import projectPFE1.repository.ReviewRepo;
import projectPFE1.repository.RoleRepo;
import projectPFE1.services.ReviewInterface;
import projectPFE1.services.UserInterface;

import java.util.List;

@Service
public class ReviewService implements ReviewInterface {
    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    UserInterface userInterface;

    @Autowired
    RoleRepo roleRepo;


    @Override
    public Review addReview(Review review) {
        if (review.getCustomer() == null || review.getTransporter() == null) {
            throw new RuntimeException("Customer and Transporter must be specified in the review.");
        }

        // Retrieve customer and transporter from DB
        UserEntity customer = userInterface.getUser(review.getCustomer().getId());
        UserEntity transporter = userInterface.getUser(review.getTransporter().getId());

        if (customer == null) {
            throw new RuntimeException("Customer not found with ID: " + review.getCustomer().getId());
        }
        if (transporter == null) {
            throw new RuntimeException("Transporter not found with ID: " + review.getTransporter().getId());
        }

        // Ensure they have correct roles
        if (!customer.getRole().stream().anyMatch(role -> role.getRolename().equals(RoleName.CUSTOMER))) {
            throw new RuntimeException("User with ID " + review.getCustomer().getId() + " is not a Customer.");
        }
        if (!transporter.getRole().stream().anyMatch(role -> role.getRolename().equals(RoleName.TRANSPORTER))) {
            throw new RuntimeException("User with ID " + review.getTransporter().getId() + " is not a Transporter.");
        }

        // Set actual references
        review.setCustomer(customer);
        review.setTransporter(transporter);

        return reviewRepo.save(review);
    }

    @Override
    public Review getReview(Long id) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review with ID " + id + " not found"));

        // Get customer and transporter summaries using the getUserSummary method
        UserEntity customerSummary = userInterface.getUserSummary(review.getCustomer().getId());
        UserEntity transporterSummary = userInterface.getUserSummary(review.getTransporter().getId());

        // Set the customer and transporter summary in the review object
        review.setCustomer(customerSummary);
        review.setTransporter(transporterSummary);

        return review;
    }

    public Review updateReview(Long reviewId, Long customerId, int newRating, String newReview) {
        // Retrieve the review by ID
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID " + reviewId));

        // Check if the review belongs to the given customer (i.e., the review's customer should match the provided customer ID)
        if (!review.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException("You can only update your own review.");
        }



        // Validate the new rating value
        if (newRating < 1 || newRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        // Update the review and rating
        review.setRating(newRating);
        review.setReview(newReview);

        // Save the updated review to the database
        return reviewRepo.save(review);
    }

    public void deleteReview(Long reviewId, Long customerId) {
        // Find the review by ID
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID " + reviewId));

        // Check if the review belongs to the customer by comparing customerId in the review
        if (!review.getCustomer().getId().equals(customerId)) {
            throw new IllegalArgumentException("You can only delete your own review.");
        }

        // Delete the review
        reviewRepo.delete(review);
    }

    @Override
    public List<Review> getReviewsByTransporterId(Long transporterId) {
        // Check if the transporter exists
        UserEntity transporter = userInterface.getUser(transporterId);
        if (transporter == null || !transporter.getRole().stream().anyMatch(role -> role.getRolename().equals(RoleName.TRANSPORTER))) {
            throw new IllegalArgumentException("Transporter with ID " + transporterId + " not found.");
        }

        // Get reviews by transporter ID
        List<Review> reviews = reviewRepo.findByTransporterId(transporterId);

        // Check if the transporter has reviews
        if (reviews.isEmpty()) {
            throw new IllegalArgumentException("No reviews found for transporter with ID " + transporterId);
        }

        // Set the customer summary (basic information) for each review
        reviews.forEach(review -> {
            UserEntity customerSummary = userInterface.getUserSummary(review.getCustomer().getId());
            review.setCustomer(customerSummary);
        });

        return reviews;
    }

    @Override
    public List<Review> getReviewsByCustomerId(Long customerId) {
        // Check if the customer exists
        UserEntity customer = userInterface.getUser(customerId);
        if (customer == null || !customer.getRole().stream().anyMatch(role -> role.getRolename().equals(RoleName.CUSTOMER))) {
            throw new IllegalArgumentException("Customer with ID " + customerId + " not found or is not a valid CUSTOMER.");
        }

        // Get reviews by customer ID
        List<Review> reviews = reviewRepo.findByCustomerId(customerId);

        // Check if the customer has reviews
        if (reviews.isEmpty()) {
            throw new IllegalArgumentException("No reviews found for customer with ID " + customerId);
        }

        // Set the transporter summary (basic information) for each review
        reviews.forEach(review -> {
            UserEntity transporterSummary = userInterface.getUserSummary(review.getTransporter().getId());
            review.setTransporter(transporterSummary);
        });

        return reviews;
    }

    @Override
    public double getAverageRatingByTransporterId(Long transporterId) {
        // Check if the transporter exists
        UserEntity transporter = userInterface.getUser(transporterId);
        if (transporter == null || !transporter.getRole().stream()
                .anyMatch(role -> role.getRolename().equals(RoleName.TRANSPORTER))) {
            throw new IllegalArgumentException("Transporter with ID " + transporterId + " not found.");
        }

        // Retrieve the average rating
        double avgRating = reviewRepo.findAverageRatingByTransporterId(transporterId);

        // If there are no reviews, return 0 instead of throwing an exception
        return avgRating;
    }



}
