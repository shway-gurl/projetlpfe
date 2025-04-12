package projectPFE1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectPFE1.entities.Proposal;
import projectPFE1.entities.TransportPosting;
import projectPFE1.serviceImplement.ProposalService;
import projectPFE1.services.ProposalInterface;

import java.util.List;

@RestController
@RequestMapping("/Proposal")
public class ProposalController {
    @Autowired
    ProposalInterface proposalInterface;

    @Autowired
    private ProposalService proposalService;

    // Create a new proposal
    @PostMapping("/createProposal")
    public ResponseEntity<Proposal> createProposal(@RequestBody Proposal proposal) {
        Proposal createdProposal = proposalService.createProposal(proposal);
        return new ResponseEntity<>(createdProposal, HttpStatus.CREATED);
    }

    //Delete a proposal
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProposal(@PathVariable Long id) {
        proposalService.deleteProposal(id);
        return ResponseEntity.noContent().build();
    }

    //Get all proposals
    @GetMapping("/getAllProposals")
    public List<Proposal> getAllProposals() {
        return proposalInterface.getAllProposals();
    }

    // Get a proposal by ID
    @GetMapping("/getProposalById/{id}")
    public Proposal getProposalById(@PathVariable Long id) {
        return proposalInterface.getProposalById(id);
    }

}
