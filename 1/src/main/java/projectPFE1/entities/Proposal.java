package projectPFE1.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TransportQuotes")
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Price breakdown (keep your existing fields)
    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Double fuelCost;

    @Column(nullable = false)
    private Double serviceFee;

    @Column(nullable = false)
    private Double tax;

    @Column(nullable = false)
    private Double additionalCharges;

    @Column(nullable = false)
    private Double totalPrice; // Should equal basePrice + fuelCost + serviceFee + tax + additionalCharges

    @Column(nullable = true)
    private String notes;

    @Column(nullable = false)
    private String estimatedDeliveryTime;

    // Status tracking (new field)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalStatus status = ProposalStatus.PENDING;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "transporter_id", nullable = false)
    private UserEntity transporter;

    @ManyToOne
    @JoinColumn(name = "posting_id", nullable = false)
    private TransportPosting posting; // Link to the posting this proposal responds to

    // Status enum (new)
    public enum ProposalStatus {
        PENDING,    // Waiting for customer review
        ACCEPTED,   // Customer selected this proposal
        REJECTED,   // Customer declined
        WITHDRAWN   // Transporter canceled offer
    }

    // Auto-calculate total price before save
    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        this.totalPrice = basePrice + fuelCost + serviceFee + tax + additionalCharges;
    }
}