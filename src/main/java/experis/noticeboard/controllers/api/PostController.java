package experis.noticeboard.controllers.api;

import java.util.ArrayList;
import java.util.Collection;

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
public class PostController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    CommentRepository commentRepository;
    @CrossOrigin()
    @GetMapping("/api/fetch/post/{id}") 
    public ResponseEntity<Post> getPostById(HttpServletRequest request, @PathVariable Integer id) {
        Post post;
        HttpStatus response;

        if (postRepository.existsById(id)) {
            System.out.println("Post with id: " + id);
            post = postRepository.findById(id).get();
            response = HttpStatus.OK;
        } else {
            System.out.println("Could not find post with id: " + id);
            post = null;
            response = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(post, response);
    }
    @CrossOrigin()
    @GetMapping("/api/fetch/post/all")
    public ResponseEntity<ArrayList<Post>> getAllPosts(HttpServletRequest request) {
        ArrayList<Post> posts = new ArrayList<Post>();
        HttpStatus response;

        posts = (ArrayList<Post>)postRepository.findAll();
        response = HttpStatus.OK;
        System.out.println("Fetched all posts");

        return new ResponseEntity<>(posts, response);
    }
    @CrossOrigin()
    @PostMapping("/api/create/post/{userId}")
    public ResponseEntity<Post> addPost(HttpServletRequest request, @RequestBody Post newPost, @PathVariable Integer userId) {
        HttpStatus response;
        if (userAccountRepository.existsById(userId)) {
            UserAccount user = userAccountRepository.findById(userId).get();
            user.getPosts().add(newPost);
            newPost.setUserAccount(user);
            newPost = postRepository.save(newPost);
            response = HttpStatus.CREATED;
            System.out.println("New post with id: " + newPost.getId() + " added");
        } else {
            System.out.println("Could not find user with id: " + userId);
            response = HttpStatus.NOT_FOUND;
        }
        
        return new ResponseEntity<>(newPost, response);
    }
    @CrossOrigin()
    @PatchMapping("/api/update/post/{id}")
    public ResponseEntity<Post> updatePost(HttpServletRequest request, @RequestBody Post newPost, @PathVariable Integer id) {
        Post post;
        HttpStatus response;

        if (postRepository.existsById(id)) {
            post = postRepository.findById(id).get();
            if (newPost.getMessage() != null) {
                post.setMessage(newPost.getMessage());
            }
            postRepository.save(post);
            response = HttpStatus.OK;
            System.out.println("Updated post with id: " + post.getId());
        } else {
            System.out.println("Could not find post with id: " + id);
            post = null;
            response = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(post, response);
    }
    @CrossOrigin()
    @DeleteMapping("/api/delete/post/{id}")
    public ResponseEntity<String> detelePost(HttpServletRequest request, @PathVariable Integer id) {
        String message = "";
        HttpStatus response;

        if (postRepository.existsById(id)) {
            Post post = postRepository.findById(id).get();
            Collection<Comment> comments = post.getComments();
            for (Comment comment : comments) {
                comment.getUserAccount().getComments().remove(comment);
                comment.getPost().getComments().remove(comment);
                commentRepository.deleteById(comment.getId());
            }
            comments.removeAll(comments);
            post.getUserAccount().getPosts().remove(post);
            postRepository.deleteById(id);
            System.out.println("Deleted post with id: " + id);
            message = "SUCCESS";
            response = HttpStatus.OK;
        } else {
            System.out.println("Could not find post with id: " + id);
            message = "FAIL";
            response = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(message, response);
    }
}
