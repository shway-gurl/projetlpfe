package projectPFE1.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectPFE1.entities.Proposal;
import projectPFE1.entities.UserEntity;
import projectPFE1.repository.UserRepo;
import projectPFE1.services.UserInterface;
import java.util.List;


@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    UserInterface userInterface;

    //add users
    @PostMapping("/add")
    public UserEntity addUser(@Valid @RequestBody UserEntity user)
    {
        return userInterface.addUser(user);
    }

    //get user by id
    @GetMapping("getUserById/{id}")
    public UserEntity getUserById(@PathVariable Long id)
    {
        return userInterface.getUser(id);
    }

    // Get the user summary
    @GetMapping("/{id}/summary")
    public UserEntity getUserSummary(@PathVariable Long id) {
        return userInterface.getUserSummary(id);
    }

    //delete user by id
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id){
        userInterface.deleteUser(id);
    }

    //add list of users
    @PostMapping("/addListOfUsers")
    public List<UserEntity> addlistusers(@RequestBody List<UserEntity> users)
    {
        return userInterface.addListUsers(users);
    }

    //find if user exist or not by username
    @GetMapping("/exists/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userInterface.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    //display user details if user exists
    @GetMapping("/find/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        UserEntity user = userInterface.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    /*
    //add user if its username not already exist
    @PostMapping("/adduserWTUN")
    public String addUserWTUN(@RequestBody UserEntity user)
    {
        return userInterface.addUserWTUN(user);
    }
    */

    //update a user
    @PutMapping("/updateUser/{id}")
    public UserEntity updateuser(@PathVariable("id")Long id,@RequestBody UserEntity user)
    {return userInterface.updateUser(id,user);}

    //Get all users
    @GetMapping("/getAllusers")
    public List<UserEntity> getAllUsers() {
        return userInterface.getAllusers();
    }

}
