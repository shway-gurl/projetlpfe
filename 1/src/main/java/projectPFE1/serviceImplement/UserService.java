package projectPFE1.serviceImplement;

//fih e logic mtaa l app

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectPFE1.entities.Role;
import projectPFE1.entities.RoleName;
import projectPFE1.entities.UserEntity;
import projectPFE1.repository.RoleRepo;
import projectPFE1.repository.UserRepo;
import projectPFE1.services.UserInterface;
import projectPFE1.utils.OpenRouteServiceUtil;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserInterface {
    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;


    @Autowired
    private OpenRouteServiceUtil openRouteServiceUtil;

    @Override
    public UserEntity getUser(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public UserEntity getUserSummary(Long userId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID " + userId));

        // Create a new UserEntity object with only the necessary fields
        UserEntity userSummary = new UserEntity();
        userSummary.setId(user.getId());
        userSummary.setFirstName(user.getFirstName());
        userSummary.setLastName(user.getLastName());


        userSummary.setInsuranceCoverage(null);
        userSummary.setRole(new HashSet<>());

        return userSummary;
    }

    @Override
    public UserEntity addUser(UserEntity user) {
        // Validate or enrich country, city, and address using ORS
        if (user.getCountry() != null) {
            // Fetch country suggestions
            String[] countrySuggestions = openRouteServiceUtil.getCountrySuggestions(user.getCountry());
            if (countrySuggestions.length > 0) {
                user.setCountry(countrySuggestions[0]); // Use the first suggestion
            }

            if (user.getCity() != null) {
                // Fetch city suggestions that belong to the selected country
                String[] citySuggestions = openRouteServiceUtil.getCitySuggestions(user.getCity(), user.getCountry());
                if (citySuggestions.length > 0) {
                    user.setCity(citySuggestions[0]); // Use the first suggestion
                }

                if (user.getAddress() != null) {
                    // Fetch address suggestions that belong to the selected city and country
                    String[] addressSuggestions = openRouteServiceUtil.getAddressSuggestions(user.getAddress(), user.getCity(), user.getCountry());
                    if (addressSuggestions.length > 0) {
                        user.setAddress(addressSuggestions[0]); // Use the first suggestion
                    }
                }
            }
        }

        // Handle roles and save user (existing logic)
        Set<Role> roles = new HashSet<>();
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            for (Role role : user.getRole()) {
                Role foundRole = roleRepo.findByRolename(role.getRolename());
                if (foundRole != null) {
                    roles.add(foundRole);
                }
            }
        }
        if (roles.isEmpty()) {
            Role customerRole = roleRepo.findByRolename(RoleName.CUSTOMER);
            roles.add(customerRole);
        }
        user.setRole(roles);

        return userRepo.save(user);
    }


    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public List<UserEntity> addListUsers(List<UserEntity> users_list) {
        return userRepo.saveAll(users_list);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    /*
    @Override
    public String addUserWTUN(UserEntity user) {
        String ch="";
        if(userRepo.existsByUsername(user.getUsername()))
        {
            ch=" user already exists";
        }else {
            userRepo.save(user);
            ch="user added !!" ;
        }
        return ch;
    }
    */

    @Override
    public UserEntity updateUser(Long id, UserEntity user) {
        // Find the user by ID
        UserEntity updatedUser = userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        // Update the user fields if they are provided in the request
        if (user.getFirstName() != null) {
            updatedUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            updatedUser.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            updatedUser.setPassword(user.getPassword());
        }
        if (user.getUsername() != null) {
            updatedUser.setUsername(user.getUsername());
        }
        if (user.getPhoneNumber() != null) {
            updatedUser.setPhoneNumber(user.getPhoneNumber());
        }

        // Validate and enrich country, city, and address using OpenRouteService
        if (user.getCountry() != null) {
            // Fetch country suggestions
            String[] countrySuggestions = openRouteServiceUtil.getCountrySuggestions(user.getCountry());
            if (countrySuggestions.length > 0) {
                updatedUser.setCountry(countrySuggestions[0]); // Use the first suggestion
            }
        }

        if (user.getCity() != null) {
            // Fetch city suggestions that belong to the selected country
            String[] citySuggestions = openRouteServiceUtil.getCitySuggestions(user.getCity(), updatedUser.getCountry());
            if (citySuggestions.length > 0) {
                updatedUser.setCity(citySuggestions[0]); // Use the first suggestion
            }
        }

        if (user.getAddress() != null) {
            // Fetch address suggestions that belong to the selected city and country
            String[] addressSuggestions = openRouteServiceUtil.getAddressSuggestions(user.getAddress(), updatedUser.getCity(), updatedUser.getCountry());
            if (addressSuggestions.length > 0) {
                updatedUser.setAddress(addressSuggestions[0]); // Use the first suggestion
            }
        }

        if (user.getTransporterType() != null) {
            updatedUser.setTransporterType(user.getTransporterType());
        }
        if (user.getCompanyName() != null) {
            updatedUser.setCompanyName(user.getCompanyName());
        }
        if (user.getVehicleCapacity() != null) {
            updatedUser.setVehicleCapacity(user.getVehicleCapacity());
        }
        if (user.getVehicleVolume() != null) {
            updatedUser.setVehicleVolume(user.getVehicleVolume());
        }
        if (user.getInsuranceCoverage() != null) {
            updatedUser.setInsuranceCoverage(user.getInsuranceCoverage());
        }
        if (user.getOperatingRegions() != null) {
            updatedUser.setOperatingRegions(user.getOperatingRegions());
        }
        if (user.getRating() != null) {
            updatedUser.setRating(user.getRating());
        }
        if (user.getAvailabilityStatus() != null) {
            updatedUser.setAvailabilityStatus(user.getAvailabilityStatus());
        }
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            updatedUser.setRole(user.getRole());
        }

        // Save the updated user
        return userRepo.save(updatedUser);
    }
    @Override
    public List<UserEntity> getAllusers() {
        return List.of();
    }



}
