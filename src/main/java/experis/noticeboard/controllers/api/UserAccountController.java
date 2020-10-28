package experis.noticeboard.controllers.api;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

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

import experis.noticeboard.models.Comment;
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
    private PostRepository PostRepository;
    @Autowired
    private CommentRepository commentRepository;


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

    @GetMapping("/api/fetch/useraccount/all")
    public ResponseEntity<ArrayList<UserAccount>> getAllUsers(HttpServletRequest request) {
        ArrayList<UserAccount> users = new ArrayList<UserAccount>();
        HttpStatus response;

        users = (ArrayList<UserAccount>)userRepository.findAll();
        response = HttpStatus.OK;
        System.out.println("Fetched all users");

        return new ResponseEntity<>(users, response);
    }

    @PostMapping("/api/create/useraccount")
    public ResponseEntity<UserAccount> addUser(HttpServletRequest request, @RequestBody UserAccount newUser) {
        newUser = userRepository.save(newUser);
        System.out.println("New user with id: " + newUser.getId() + " added");
        HttpStatus response = HttpStatus.CREATED;
        return new ResponseEntity<>(newUser, response);
    }

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
            if (newUser.getPosts() != null) {
                user.setPosts(newUser.getPosts());
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

    @DeleteMapping("/api/delete/useraccount/{id}")
    public ResponseEntity<String> deteleUser(HttpServletRequest request, @PathVariable Integer id) {
        String message = "";
        HttpStatus response;

        if (userRepository.existsById(id)) {
            UserAccount user = userRepository.findById(id).get();
            Collection<Comment> comments = user.getComments();
            for (Comment comment : comments) {
                comment.getPost().getComments().remove(comment);
            }
            comments.clear();
            Collection<Post> posts = user.getPosts();
            for (Post post : posts) {
                comments = post.getComments();
                for (Comment comment : comments) {
                    comment.getUserAccount().getComments().remove(comment);
                }
                comments.clear();
            }
            user.getPosts().clear();
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
