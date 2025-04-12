package projectPFE1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "TransportPostings")
public class TransportPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Column(nullable = false)
    private String pickUpLocation;

    @Column(nullable = false)
    private String dropOffLocation;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date pickupDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dropoffDate;

    @Column(nullable = false)
    private String itemDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportPosting.Categories category;

    public enum Categories {
        Furniture,
        Boxes,
        Cars,
        Motorcycles,
        Haulage,
        Pets,
        Livestock,
        Other
    }

    @Column(nullable = true)
    @Min(value = 0, message = "Weight must be positive")
    private double weight;

    @Column(nullable = true)
    @Min(value = 0, message = "Volume must be positive")
    private double volume;

    @Column(nullable = true)
    private String dimensions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private TransportPosting.Fragility fragility;

    public enum Fragility {
        LOW,
        MEDIUM,
        HIGH,
        FRAGILE,
        NON_FRAGILE
    }

    @Column(nullable = true)
    private boolean temperatureControl;

    @Column(nullable = false)
    private boolean stackable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportPosting.PreferredPaymentMethod preferredPaymentMethod;

    public enum PreferredPaymentMethod {
        CASH,
        CREDIT_CARD,
        DEBIT_CARD,
        PAYPAL,
        OTHER
    }

    private LocalDateTime expiryTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)  // Make sure it's non-nullable
    private PostingStatus postingStatus;
    public enum PostingStatus {
        OPEN, CLOSED, EXPIRED
    }

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private UserEntity customer;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<Proposal> proposals = new ArrayList<>();

}
