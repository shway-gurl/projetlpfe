package projectPFE1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectPFE1.entities.Proposal;

public interface ProposalRepo extends JpaRepository<Proposal, Long> {
}
