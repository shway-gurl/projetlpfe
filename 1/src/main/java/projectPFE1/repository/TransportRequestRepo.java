package projectPFE1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectPFE1.entities.TransportRequest;

@Repository
public interface TransportRequestRepo extends JpaRepository<TransportRequest, Long> {
}
