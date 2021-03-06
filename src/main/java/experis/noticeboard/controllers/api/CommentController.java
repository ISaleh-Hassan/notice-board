package experis.noticeboard.controllers.api;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import experis.noticeboard.models.Comment;
import experis.noticeboard.models.Post;
import experis.noticeboard.models.UserAccount;
import experis.noticeboard.repositories.CommentRepository;
import experis.noticeboard.repositories.PostRepository;
import experis.noticeboard.repositories.UserAccountRepository;


@RestController
public class CommentController {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserAccountRepository userRepository;
    @Autowired
    PostRepository postRepositroy;
    @CrossOrigin()

    @GetMapping("/api/fetch/comment/{id}") 
    public ResponseEntity<Comment> getCommentById(@PathVariable Integer id) {
        try {
            return commentRepository.findById(id)
            .map(comment -> new ResponseEntity<>(comment, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>((Comment) null, HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            System.out.println("id was null");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin()
    @GetMapping("/api/fetch/comment/all")
    public ResponseEntity<ArrayList<Comment>> getAllComments(HttpServletRequest request) {
        ArrayList<Comment> comments = (ArrayList<Comment>)commentRepository.findAll();
        System.out.println("Fetched all comments");
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/api/create/comment/{userId}/{postId}")
    public ResponseEntity<Comment> addComment(@RequestBody Comment newComment, @PathVariable Integer userId, @PathVariable Integer postId) {
        try {
            HttpStatus response;
            if (userRepository.existsById(userId) && postRepositroy.existsById(postId)) {
                newComment.setUserAccount(new UserAccount(userId));
                newComment.setPost(new Post(postId));
                newComment = commentRepository.save(newComment);
                System.out.println("New comment with id: " + newComment.getId() + " added");
                response = HttpStatus.CREATED;
            } else {
                System.out.println("Could not find user with id: " + userId + " or post with id: " + postId);
                response = HttpStatus.NOT_FOUND;
                newComment = null;
            }
            return new ResponseEntity<>(newComment, response);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown: id or newComment was null.");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin()
    @PatchMapping("/api/update/comment/{id}")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment newComment, @PathVariable Integer id) {
       try {
            Comment comment = commentRepository.findById(id).orElse(null);
            HttpStatus response;
            if (comment != null) {
                if (newComment.getMessage() != null) {
                    comment.setMessage(newComment.getMessage());
                }
                commentRepository.save(comment);
                response = HttpStatus.OK;
                System.out.println("Updated comment with id: " + comment.getId());
            } else {
                System.out.println("Could not find comment with id: " + id);
                response = HttpStatus.NOT_FOUND;
            }
            return new ResponseEntity<>(comment, response);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown: id was null");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
       }   
    }
    @CrossOrigin()
    @DeleteMapping("/api/delete/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer id) {
        try {
            String message = "";
            HttpStatus response;
    
            if (commentRepository.existsById(id)) {
                commentRepository.deleteById(id);
                System.out.println("Deleted comment with id: " + id);
                message = "SUCCESS";
                response = HttpStatus.OK;
            } else {
                System.out.println("Could not find comment with id: " + id);
                message = "FAIL";
                response = HttpStatus.NOT_FOUND;
            }
            return new ResponseEntity<>(message, response);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown: id was null");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }    
    }
}
