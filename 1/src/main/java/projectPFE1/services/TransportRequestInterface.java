package projectPFE1.services;

import projectPFE1.entities.TransportRequest;
import java.util.List;

public interface TransportRequestInterface {
    TransportRequest addRequest(TransportRequest transportRequest);
    public TransportRequest getRequestById(Long id);
    public List<TransportRequest> getAllRequests();
    public TransportRequest updateRequest(Long id, TransportRequest transportRequest);
    public void deleteRequest(Long id);
}
