package experis.noticeboard.controllers.api;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import experis.noticeboard.models.UserAccount;
import experis.noticeboard.repositories.UserAccountRepository;

@RestController
public class UserAccountController {
    
    @Autowired
    private UserAccountRepository userRepository;
    @CrossOrigin()
    @GetMapping("/api/fetch/useraccount/{id}") 
    public ResponseEntity<UserAccount> getUserById(HttpServletRequest request, @PathVariable Integer id) {
        UserAccount user;
        HttpStatus response;

        if (userRepository.existsById(id)) {
            System.out.println("User with id: " + id);
            user = userRepository.findById(id).get();
            response = HttpStatus.OK;
        } else {
            System.out.println("Could not find user with id: " + id);
            user = null;
            response = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(user, response);
    }
    @CrossOrigin()
    @GetMapping("/api/fetch/useraccount/all")
    public ResponseEntity<ArrayList<UserAccount>> getAllUsers(HttpServletRequest request) {
        ArrayList<UserAccount> users = new ArrayList<UserAccount>();
        HttpStatus response;

        users = (ArrayList<UserAccount>)userRepository.findAll();
        response = HttpStatus.OK;
        System.out.println("Fetched all users");

        return new ResponseEntity<>(users, response);
    }
    @CrossOrigin()
    @PostMapping("/api/create/useraccount")
    public ResponseEntity<UserAccount> addUser(HttpServletRequest request, @RequestBody UserAccount newUser) {
        newUser = userRepository.save(newUser);
        System.out.println("New user with id: " + newUser.getId() + " added");
        HttpStatus response = HttpStatus.CREATED;
        return new ResponseEntity<>(newUser, response);
    }
    @CrossOrigin()
    @PatchMapping("/api/update/useraccount/{id}")
    public ResponseEntity<UserAccount> updateUser(HttpServletRequest request, @RequestBody UserAccount newUser, @PathVariable Integer id) {
        UserAccount user;
        HttpStatus response;

        if (userRepository.existsById(id)) {
            user = userRepository.findById(id).get();

            if (newUser.getUsername() != null) {
                user.setUsername(newUser.getUsername());
            }
            if (newUser.getPassword() != null) {
                user.setPassword(newUser.getPassword());
            }

            userRepository.save(user);
            response = HttpStatus.OK;
            System.out.println("Updated user with id: " + user.getId());
        } else {
            System.out.println("Could not find user with id: " + id);
            user = null;
            response = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(user, response);
    }
    @CrossOrigin()
    @DeleteMapping("/api/delete/useraccount/{id}")
    public ResponseEntity<String> deteleUser(HttpServletRequest request, @PathVariable Integer id) {
        String message = "";
        HttpStatus response;

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            System.out.println("Deleted user with id: " + id);
            message = "SUCCESS";
            response = HttpStatus.OK;
        } else {
            System.out.println("Could not find user with id: " + id);
            message = "FAIL";
            response = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(message, response);
    }
    
}
