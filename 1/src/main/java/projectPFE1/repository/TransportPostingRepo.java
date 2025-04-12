package projectPFE1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectPFE1.entities.TransportPosting;

import java.util.Optional;

@Repository
public interface TransportPostingRepo extends JpaRepository<TransportPosting, Long> {
    Optional<TransportPosting> findById(Long id);
}
