package projectPFE1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectPFE1.entities.TransportPosting;
import projectPFE1.entities.TransportRequest;
import projectPFE1.serviceImplement.TransportPostingService;
import projectPFE1.services.TransportPostingInterface;
import projectPFE1.services.UserInterface;
import projectPFE1.utils.OpenRouteServiceUtil;

import java.util.List;

@RestController
@RequestMapping("/TransportPosting")
public class TransportPostingController {

    @Autowired
    TransportPostingInterface transportPostingInterface;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private TransportPostingService transportPostingService;

    @Autowired
    private OpenRouteServiceUtil openRouteServiceUtil;

    //Create a posting
    @PostMapping("/addPosting")
    public ResponseEntity<TransportPosting> addTransportPosting(@RequestBody TransportPosting transportPosting) {
        TransportPosting savedRequest = transportPostingService.addPosting(transportPosting);
        return ResponseEntity.ok(savedRequest);
    }

    //Delete a posting
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransportPosting(@PathVariable Long id) {
        transportPostingService.deletePosting(id);
        return ResponseEntity.noContent().build();
    }

    // Get transport request by ID
    @GetMapping("/getPostingById/{id}")
    public TransportPosting getPostingById(@PathVariable Long id) {
        return transportPostingInterface.getPostingById(id);
    }

    // Get all transport postings
    @GetMapping("/getAllPostings")
    public List<TransportPosting> getAllPostings() {
        return transportPostingInterface.getAllPostings();
    }

    //Update posting
    @PutMapping("/updatePosting/{id}")
    public ResponseEntity<TransportPosting> updatePosting(
            @PathVariable Long id,
            @RequestBody TransportPosting transportPosting) {
        try {
            TransportPosting updatedPosting = transportPostingService.updatePosting(id, transportPosting);
            return ResponseEntity.ok(updatedPosting);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Retrieve transport posting with proposal
    @GetMapping("/getProposalForPosting/{id}")
    public ResponseEntity<TransportPosting> getTransportPostingWithProposals(@PathVariable Long id) {
        TransportPosting transportPosting = transportPostingService.getTransportPostingWithProposals(id);
        return ResponseEntity.ok(transportPosting);
    }
}
