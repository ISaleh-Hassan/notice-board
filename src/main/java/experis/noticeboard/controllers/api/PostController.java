package experis.noticeboard.controllers.api;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import experis.noticeboard.models.Post;
import experis.noticeboard.repositories.PostRepository;

@RestController
public class PostController {
    @Autowired
    PostRepository postRepository;
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
    @PostMapping("/api/create/post")
    public ResponseEntity<Post> addPost(HttpServletRequest request, @RequestBody Post newPost) {
        newPost = postRepository.save(newPost);
        System.out.println("New post with id: " + newPost.getId() + " added");
        HttpStatus response = HttpStatus.CREATED;
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
            if (newPost.getUserAccountId() != null) {
                post.setUserAccountId(newPost.getUserAccountId());
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
