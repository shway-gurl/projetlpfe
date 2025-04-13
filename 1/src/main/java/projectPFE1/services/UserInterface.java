package projectPFE1.services;

import org.apache.catalina.User;
import projectPFE1.entities.UserEntity;

import java.util.List;

public interface UserInterface {
    UserEntity addUser(UserEntity user);
    void deleteUser(Long id);
    List<UserEntity> addListUsers(List<UserEntity> users_list);
    boolean existsByUsername(String username);
    UserEntity findByUsername(String username);
    public UserEntity updateUser(Long id,UserEntity user);
    public List<UserEntity> getAllusers();
    UserEntity getUser(Long id);
    UserEntity getUserSummary(Long userId);

}
