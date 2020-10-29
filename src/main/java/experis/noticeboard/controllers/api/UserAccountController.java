package experis.noticeboard.controllers.api;

import java.util.ArrayList;
import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import experis.noticeboard.models.Post;
import experis.noticeboard.models.UserAccount;
import experis.noticeboard.repositories.CommentRepository;
import experis.noticeboard.repositories.PostRepository;
import experis.noticeboard.repositories.UserAccountRepository;

@RestController
public class UserAccountController {
    
    @Autowired
    private UserAccountRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/api/fetch/useraccount/{id}") 
    public ResponseEntity<UserAccount> getUserById(@PathVariable Integer id) {
        try {
            return userRepository.findById(id)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>((UserAccount) null, HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown: id was null");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/fetch/useraccount/all")
    public ResponseEntity<ArrayList<UserAccount>> getAllUsers() {
        ArrayList<UserAccount> users = (ArrayList<UserAccount>)userRepository.findAll();
        HttpStatus response = HttpStatus.OK;
        System.out.println("Fetched all users");
        return new ResponseEntity<>(users, response);
    }

    @PostMapping("/api/create/useraccount")
    public ResponseEntity<UserAccount> addUser(@RequestBody UserAccount newUser) {
        try {
            newUser = userRepository.save(newUser);
            System.out.println("New user with id: " + newUser.getId() + " added");
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown: newUser was null.");
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }    
    }

    @PatchMapping("/api/update/useraccount/{id}")
    public ResponseEntity<UserAccount> updateUser(@RequestBody UserAccount newUser, @PathVariable Integer id) {      
        try {
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
                if (newUser.getPosts() != null) {
                    user.setPosts(newUser.getPosts());
                }
                if (newUser.getComments() != null) {
                    user.setComments(newUser.getComments());
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
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown: id or user was null.");
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }     
    }

    @DeleteMapping("/api/delete/useraccount/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {       
        try {
            String message = "";
            HttpStatus response;
    
            if (userRepository.existsById(id)) {
                UserAccount user = userRepository.findById(id).get();
                commentRepository.deleteAll(user.getComments());
                Collection<Post> posts = user.getPosts();
                if (posts.size() > 0) {
                    for (Post post : posts) {
                        commentRepository.deleteAll(post.getComments());
                    }
                    postRepository.deleteAll(user.getPosts());
                }
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
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown: id was null.");
            return new ResponseEntity<>("FAIL", HttpStatus.NOT_FOUND);
        }    
    }  
}
