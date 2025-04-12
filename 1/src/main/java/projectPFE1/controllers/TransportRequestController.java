package projectPFE1.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectPFE1.entities.TransportRequest;
import projectPFE1.entities.UserEntity;
import projectPFE1.serviceImplement.TransportRequestService;

import projectPFE1.services.TransportRequestInterface;
import projectPFE1.services.UserInterface;
import projectPFE1.utils.OpenRouteServiceUtil;
import java.util.List;


@RestController
@RequestMapping("/TransportRequest")
public class TransportRequestController {
    @Autowired
    TransportRequestInterface transportRequestInterface;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private TransportRequestService transportRequestService;

    @Autowired
    private OpenRouteServiceUtil openRouteServiceUtil;

    @PostMapping("/addRequest")
    public ResponseEntity<TransportRequest> addTransportRequest(@RequestBody TransportRequest transportRequest) {
        TransportRequest savedRequest = transportRequestService.addRequest(transportRequest);
        return ResponseEntity.ok(savedRequest);
    }

    // Get transport request by ID
    @GetMapping("/getRequestById/{id}")
    public TransportRequest getRequestById(@PathVariable Long id) {
        return transportRequestInterface.getRequestById(id);
    }

    // Get all transport requests
    @GetMapping("/getAllRequests")
    public List<TransportRequest> getAllRequests() {
        return transportRequestInterface.getAllRequests();
    }

    //Delete a transport request
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        transportRequestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    //Update request
    @PutMapping("/updateRequest/{id}")
    public ResponseEntity<TransportRequest> updateRequest(
            @PathVariable Long id,
            @RequestBody TransportRequest transportRequest) {
        try {
            TransportRequest updatedRequest = transportRequestService.updateRequest(id, transportRequest);
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
