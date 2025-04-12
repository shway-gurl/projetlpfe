package projectPFE1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TransportRequests")
public class TransportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Column(nullable = false)
    private String status;

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
    private Categories category;

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
    private Fragility fragility;

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
    private PreferredPaymentMethod preferredPaymentMethod;

    public enum PreferredPaymentMethod {
        CASH,
        CREDIT_CARD,
        DEBIT_CARD,
        PAYPAL,
        OTHER
    }

    private Double Price;

    @Column(nullable = true, length = 500)
    private String specialInstructions;

    @Column(nullable = true)
    private Double desiredPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private UserEntity customer;

    @ManyToOne
    @JoinColumn(name = "transporter_id", nullable = false)
    private UserEntity transporter;

}
