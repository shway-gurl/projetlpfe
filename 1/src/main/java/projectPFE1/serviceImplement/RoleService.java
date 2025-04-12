package projectPFE1.serviceImplement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectPFE1.entities.Role;
import projectPFE1.repository.RoleRepo;
import projectPFE1.services.RoleInterface;


@Service
public class RoleService implements RoleInterface {
    @Autowired
    private RoleRepo roleRepo;

    @Override
    public Role add(Role role) {
        return roleRepo.save(role);
    }
}
