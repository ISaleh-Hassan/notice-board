package experis.noticeboard.controllers.api;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import experis.noticeboard.models.Comment;
import experis.noticeboard.repositories.CommentRepository;


@RestController
public class CommentController {
    @Autowired
    CommentRepository commentRepository;
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
    @CrossOrigin()
    @PostMapping("/api/create/comment")
    public ResponseEntity<Comment> addComment(HttpServletRequest request, @RequestBody Comment newComment) {
        newComment = commentRepository.save(newComment);
        System.out.println("New comment with id: " + newComment.getId() + " added");
        HttpStatus response = HttpStatus.CREATED;
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
            if (newComment.getPostId() != null) {
                comment.setPostId(newComment.getPostId());
            }
            if (newComment.getUserAccountId() != null) {
                comment.setUserAccountId(newComment.getUserAccountId());
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
