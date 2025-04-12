package projectPFE1.repository;

//interaction maa l db (dao)

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projectPFE1.entities.UserEntity;
import projectPFE1.services.UserInterface;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    UserEntity findByUsername(String username);

}
