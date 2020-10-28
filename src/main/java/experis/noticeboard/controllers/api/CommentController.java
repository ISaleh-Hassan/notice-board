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
    public ResponseEntity<Comment> getCommentById(HttpServletRequest request, @PathVariable Integer id) {
        Comment comment;
        HttpStatus response;

        if (commentRepository.existsById(id)) {
            System.out.println("Comment with id: " + id);
            comment = commentRepository.findById(id).get();
            response = HttpStatus.OK;
        } else {
            System.out.println("Could not find comment with id: " + id);
            comment = null;
            response = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(comment, response);
    }
    @CrossOrigin()
    @GetMapping("/api/fetch/comment/all")
    public ResponseEntity<ArrayList<Comment>> getAllComments(HttpServletRequest request) {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        HttpStatus response;

        comments = (ArrayList<Comment>)commentRepository.findAll();
        response = HttpStatus.OK;
        System.out.println("Fetched all comments");

        return new ResponseEntity<>(comments, response);
    }

    @PostMapping("/api/create/comment/{userId}/{postId}")
    public ResponseEntity<Comment> addComment(HttpServletRequest request, @RequestBody Comment newComment, @PathVariable Integer userId, @PathVariable Integer postId) {
        HttpStatus response;
        if (userRepository.existsById(userId) && postRepositroy.existsById(postId)) {
            UserAccount user = userRepository.findById(userId).get();
            Post post = postRepositroy.findById(postId).get();
            newComment.setUserAccount(user);
            newComment.setPost(post);
            post.getComments().add(newComment);
            user.getComments().add(newComment);
            newComment = commentRepository.save(newComment);
            System.out.println("New comment with id: " + newComment.getId() + " added");
            response = HttpStatus.CREATED;
        } else {
            System.out.println("Could not find user with id: " + userId + " or post with id: " + postId);
            response = HttpStatus.NOT_FOUND;
            newComment = null;
        }
        return new ResponseEntity<>(newComment, response);
    }
    @CrossOrigin()
    @PatchMapping("/api/update/comment/{id}")
    public ResponseEntity<Comment> updateComment(HttpServletRequest request, @RequestBody Comment newComment, @PathVariable Integer id) {
        Comment comment;
        HttpStatus response;

        if (commentRepository.existsById(id)) {
            comment = commentRepository.findById(id).get();

            if (newComment.getMessage() != null) {
                comment.setMessage(newComment.getMessage());
            }
            commentRepository.save(comment);
            response = HttpStatus.OK;
            System.out.println("Updated comment with id: " + comment.getId());
        } else {
            System.out.println("Could not find comment with id: " + id);
            comment = null;
            response = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(comment, response);
    }
    @CrossOrigin()
    @DeleteMapping("/api/delete/comment/{id}")
    public ResponseEntity<String> deteleComment(HttpServletRequest request, @PathVariable Integer id) {
        String message = "";
        HttpStatus response;

        if (commentRepository.existsById(id)) {
            Comment comment = commentRepository.findById(id).get();
            comment.getPost().getComments().remove(comment);
            comment.getUserAccount().getComments().remove(comment);
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
    }
}
