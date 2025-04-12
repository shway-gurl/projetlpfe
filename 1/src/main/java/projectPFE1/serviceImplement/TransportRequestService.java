package projectPFE1.serviceImplement;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectPFE1.entities.RoleName;
import projectPFE1.entities.TransportRequest;
import projectPFE1.entities.UserEntity;
import projectPFE1.repository.TransportRequestRepo;
import projectPFE1.services.TransportRequestInterface;
import projectPFE1.services.UserInterface;
import projectPFE1.utils.OpenRouteServiceUtil;

import java.util.List;

@Service
@Transactional
public class TransportRequestService implements TransportRequestInterface {

    @Autowired
    TransportRequestRepo transportRequestRepo;

    @Autowired
    UserInterface userInterface;

    @Autowired
    OpenRouteServiceUtil openRouteServiceUtil;

    //add request
    @Override
    public TransportRequest addRequest(TransportRequest transportRequest) {
        Long customerId = transportRequest.getCustomer().getId();
        UserEntity customer = userInterface.getUser(customerId);

        // Validate customer exists (without role check)
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        Long transporterId = transportRequest.getTransporter() != null ? transportRequest.getTransporter().getId() : null;
        UserEntity transporter = null;

        // If transporter ID is provided, validate transporter exists (without role check)
        if (transporterId != null) {
            transporter = userInterface.getUser(transporterId);
            if (transporter == null) {
                throw new IllegalArgumentException("Transporter not found");
            }
        }

        // Validate and set pick-up and drop-off locations using ORS suggestions
        String pickUpLocation = transportRequest.getPickUpLocation();
        String dropOffLocation = transportRequest.getDropOffLocation();

        // Get location suggestions for pick-up and drop-off locations
        String[] pickUpSuggestions = openRouteServiceUtil.getLocationSuggestions(pickUpLocation);
        String[] dropOffSuggestions = openRouteServiceUtil.getLocationSuggestions(dropOffLocation);

        // Set the closest suggestion for pick-up and drop-off locations
        if (pickUpSuggestions.length > 0) {
            transportRequest.setPickUpLocation(pickUpSuggestions[0]); // Use the first suggestion (closest match)
        } else {
            throw new IllegalArgumentException("Invalid pick-up location: No suggestions found");
        }

        if (dropOffSuggestions.length > 0) {
            transportRequest.setDropOffLocation(dropOffSuggestions[0]); // Use the first suggestion (closest match)
        } else {
            throw new IllegalArgumentException("Invalid drop-off location: No suggestions found");
        }

        // Set the customer and transporter for the transport request
        transportRequest.setCustomer(customer);
        transportRequest.setTransporter(transporter);

        // Save the transport request to the database
        return transportRequestRepo.save(transportRequest);
    }

    @Override
    public TransportRequest getRequestById(Long id) {
        return transportRequestRepo.findById(id).orElse(null);
    }

    @Override
    public List<TransportRequest> getAllRequests() {
        return transportRequestRepo.findAll();
    }

    @Override
    public TransportRequest updateRequest(Long id, TransportRequest transportRequest) {
        // Find the existing request by ID
        TransportRequest existingRequest = transportRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("TransportRequest not found with id: " + id));

        // Update the fields (excluding id and createdAt)
        existingRequest.setStatus(transportRequest.getStatus());
        existingRequest.setPickUpLocation(transportRequest.getPickUpLocation());
        existingRequest.setDropOffLocation(transportRequest.getDropOffLocation());
        existingRequest.setPickupDate(transportRequest.getPickupDate());
        existingRequest.setDropoffDate(transportRequest.getDropoffDate());
        existingRequest.setItemDescription(transportRequest.getItemDescription());
        existingRequest.setCategory(transportRequest.getCategory());
        existingRequest.setWeight(transportRequest.getWeight());
        existingRequest.setVolume(transportRequest.getVolume());
        existingRequest.setDimensions(transportRequest.getDimensions());
        existingRequest.setFragility(transportRequest.getFragility());
        existingRequest.setTemperatureControl(transportRequest.isTemperatureControl());
        existingRequest.setStackable(transportRequest.isStackable());
        existingRequest.setPreferredPaymentMethod(transportRequest.getPreferredPaymentMethod());
        existingRequest.setSpecialInstructions(transportRequest.getSpecialInstructions());

        // Save the updated request
        return transportRequestRepo.save(existingRequest);
    }

    @Override
    public void deleteRequest(Long id) {
        transportRequestRepo.deleteById(id);
    }






}
