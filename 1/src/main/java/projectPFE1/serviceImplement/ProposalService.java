package projectPFE1.serviceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectPFE1.entities.Proposal;
import projectPFE1.entities.TransportPosting;
import projectPFE1.entities.UserEntity;
import projectPFE1.repository.ProposalRepo;
import projectPFE1.repository.TransportPostingRepo;
import projectPFE1.repository.UserRepo;
import projectPFE1.services.ProposalInterface;

import java.util.List;

@Service
public class ProposalService implements ProposalInterface {
    @Autowired
    ProposalRepo proposalRepo;

    @Autowired
    TransportPostingRepo transportPostingRepo;

    @Autowired
    UserRepo userRepo;

    // Create a proposal
    @Override
    public Proposal createProposal(Proposal proposal) {
        // Fetch the TransportPosting and Transporter based on the provided IDs
        TransportPosting posting = transportPostingRepo.findById(proposal.getPosting().getId())
                .orElseThrow(() -> new RuntimeException("Transport Posting not found"));

        UserEntity transporter = userRepo.findById(proposal.getTransporter().getId())
                .orElseThrow(() -> new RuntimeException("Transporter not found"));

        // Set the TransportPosting and Transporter in the Proposal
        proposal.setPosting(posting);
        proposal.setTransporter(transporter);

        // Auto-calculate total price (based on @PrePersist in Proposal entity)
        proposal.calculateTotal();

        // Save and return the proposal
        return proposalRepo.save(proposal);
    }

    //Delete a proposal
    @Override
    public void deleteProposal(Long id) {
        proposalRepo.deleteById(id);
    }

    //Get proposal by id
    @Override
    public Proposal getProposalById(Long id) {
        return proposalRepo.findById(id).orElse(null);
    }

    //Get all proposals
    @Override
    public List<Proposal> getAllProposals() {
        return proposalRepo.findAll();
    }

}
