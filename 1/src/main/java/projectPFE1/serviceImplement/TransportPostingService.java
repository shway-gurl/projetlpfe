package projectPFE1.serviceImplement;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectPFE1.entities.TransportPosting;
import projectPFE1.entities.UserEntity;
import projectPFE1.repository.TransportPostingRepo;
import projectPFE1.repository.TransportRequestRepo;
import projectPFE1.services.TransportPostingInterface;
import projectPFE1.services.UserInterface;
import projectPFE1.utils.OpenRouteServiceUtil;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TransportPostingService implements TransportPostingInterface {
    @Autowired
    TransportPostingRepo transportPostingRepo;

    @Autowired
    UserInterface userInterface;

    @Autowired
    OpenRouteServiceUtil openRouteServiceUtil;


    @Override
    @Transactional
    public TransportPosting addPosting(TransportPosting transportPosting) {
        // Validate customer exists
        Long customerId = transportPosting.getCustomer().getId();
        UserEntity customer = userInterface.getUserSummary(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        // Validate and normalize locations using ORS
        String pickUpLocation = transportPosting.getPickUpLocation();
        String dropOffLocation = transportPosting.getDropOffLocation();

        String[] pickUpSuggestions = openRouteServiceUtil.getLocationSuggestions(pickUpLocation);
        String[] dropOffSuggestions = openRouteServiceUtil.getLocationSuggestions(dropOffLocation);

        if (pickUpSuggestions.length > 0) {
            transportPosting.setPickUpLocation(pickUpSuggestions[0]);
        } else {
            throw new IllegalArgumentException("Invalid pick-up location: No suggestions found");
        }

        if (dropOffSuggestions.length > 0) {
            transportPosting.setDropOffLocation(dropOffSuggestions[0]);
        } else {
            throw new IllegalArgumentException("Invalid drop-off location: No suggestions found");
        }

        // Set default values for a new posting
        transportPosting.setCustomer(customer);
        transportPosting.setPostingStatus(TransportPosting.PostingStatus.OPEN);
        transportPosting.setCreatedAt(new Date());

        // Set expiry time (e.g., 3 days from now)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        transportPosting.setExpiryTime(LocalDateTime.now().plusDays(3));

        // Save the posting
        return transportPostingRepo.save(transportPosting);
    }

    //Get posting by ID
    @Override
    public TransportPosting getPostingById(Long id) {
        return transportPostingRepo.findById(id).orElse(null);
    }

    //Get postings
    @Override
    public List<TransportPosting> getAllPostings() {
        return transportPostingRepo.findAll();
    }

    //Update a posting
    @Override
    public TransportPosting updatePosting(Long id, TransportPosting updatedPosting) {
        // Fetch the existing posting
        TransportPosting existingPosting = transportPostingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transport posting not found with ID: " + id));

        // Update modifiable fields
        existingPosting.setPickUpLocation(updatedPosting.getPickUpLocation());
        existingPosting.setDropOffLocation(updatedPosting.getDropOffLocation());
        existingPosting.setPickupDate(updatedPosting.getPickupDate());
        existingPosting.setDropoffDate(updatedPosting.getDropoffDate());
        existingPosting.setItemDescription(updatedPosting.getItemDescription());
        existingPosting.setCategory(updatedPosting.getCategory());
        existingPosting.setWeight(updatedPosting.getWeight());
        existingPosting.setVolume(updatedPosting.getVolume());
        existingPosting.setDimensions(updatedPosting.getDimensions());
        existingPosting.setFragility(updatedPosting.getFragility());
        existingPosting.setTemperatureControl(updatedPosting.isTemperatureControl());
        existingPosting.setStackable(updatedPosting.isStackable());
        existingPosting.setPreferredPaymentMethod(updatedPosting.getPreferredPaymentMethod());
        existingPosting.setExpiryTime(updatedPosting.getExpiryTime());
        existingPosting.setPostingStatus(updatedPosting.getPostingStatus());

        // Save and return the updated posting
        return transportPostingRepo.save(existingPosting);
    }


    //Delete posting
    @Override
    public void deletePosting(Long id) {
        transportPostingRepo.deleteById(id);
    }

    //Retrieve proposals for tranport posting by transport popsting id
    @Override
    public TransportPosting getTransportPostingWithProposals(Long id) {
        return transportPostingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transport Posting not found"));
    }
}
