package projectPFE1.services;

import projectPFE1.entities.TransportPosting;
import java.util.List;

public interface TransportPostingInterface {
    TransportPosting addPosting(TransportPosting transportPosting);
    public TransportPosting getPostingById(Long id);
    public List<TransportPosting> getAllPostings();
    public TransportPosting updatePosting(Long id, TransportPosting transportPosting);
    public void deletePosting(Long id);
    public TransportPosting getTransportPostingWithProposals(Long id);
}
