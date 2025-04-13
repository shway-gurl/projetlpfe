package projectPFE1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)

@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //COMMON USER ATTRIBUTES
    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 100, nullable = false, unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;


    @Column(nullable = false, unique = true)
    private String phoneNumber;


    @Column(length = 255, nullable = true)
    private String country;

    @Column(length = 225, nullable = true)
    private String city;

    /* @Enumerated(EnumType.STRING)
    private UserStatus status; */

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    //  CUSTOMER-SPECIFIC ATTRIBUTES

    @Column(length = 225, nullable = true)
    private String address;

    // TRANSPORTER-SPECIFIC ATTRIBUTES

    private TransporterType transporterType;

    public enum TransporterType {
        INDIVIDUAL, COMPANY
    }

    @Column(length = 100, nullable = true)
    private String companyName;

    @Column(nullable = true)
    private Double vehicleCapacity;  // kg or tons

    @Column(nullable = true)
    private Double vehicleVolume;  // cubic meters (m³)

    @Column(nullable = true)
    private Boolean insuranceCoverage = false;

    @Column(nullable = true)
    private String operatingRegions;

    @Column(nullable = true)
    private Double rating;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    public enum AvailabilityStatus {
        AVAILABLE, BUSY, UNAVAILABLE
    }

    public enum UserStatus {
        ACTIVE, SUSPENDED, DEACTIVATED
    }

    // Many-to-One: One Customer → Many TransportRequests
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransportRequest> transportRequestsAsCustomer;

    // Many-to-One: One Transporter → Many TransportRequests
    @OneToMany(mappedBy = "transporter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransportRequest> transportRequestsAsTransporter;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransportPosting> transportPostings = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name ="role_id"))
    private Set<Role> role =new HashSet<>();

}