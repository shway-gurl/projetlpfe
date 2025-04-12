package projectPFE1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projectPFE1.entities.Role;
import projectPFE1.entities.RoleName;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Role findByRolename(RoleName rolename);
}
