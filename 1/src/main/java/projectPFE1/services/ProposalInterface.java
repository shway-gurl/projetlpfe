package projectPFE1.services;

import projectPFE1.entities.Proposal;
import projectPFE1.entities.TransportPosting;
import projectPFE1.entities.TransportRequest;

import java.util.List;

public interface ProposalInterface {
    Proposal createProposal(Proposal proposal);
    public void deleteProposal(Long id);
    public Proposal getProposalById(Long id);
    public List<Proposal> getAllProposals();
}
