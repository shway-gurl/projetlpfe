package projectPFE1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projectPFE1.entities.Review;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review,Long> {
    List<Review> findByTransporterId(Long transporterId);
    List<Review> findByCustomerId(Long customerId);
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.transporter.id = :transporterId")
    double findAverageRatingByTransporterId(@Param("transporterId") Long transporterId);
}
