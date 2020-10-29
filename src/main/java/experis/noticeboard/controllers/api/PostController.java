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

    @GetMapping("/api/fetch/post/{id}") 
    public ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        try {
            return postRepository.findById(id)
            .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>((Post) null, HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            System.out.println("id was null");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/fetch/post/all")
    public ResponseEntity<ArrayList<Post>> getAllPosts() {
        ArrayList<Post> posts = (ArrayList<Post>)postRepository.findAll();
        System.out.println("Fetched all posts");
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping("/api/create/post/{userId}")
    public ResponseEntity<Post> addPost(@RequestBody Post newPost, @PathVariable Integer userId) {
        try {
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
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown: id or newPost was null.");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/api/update/post/{id}")
    public ResponseEntity<Post> updatePost(@RequestBody Post newPost, @PathVariable Integer id) {    
        try {
            Post post = null;
            HttpStatus response = HttpStatus.NOT_FOUND;
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
            }
            return new ResponseEntity<>(post, response);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown: id or newPost was null.");
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }  
    }

    @DeleteMapping("/api/delete/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Integer id) {
        try {
            String message = "FAIL";
            HttpStatus response = HttpStatus.NOT_FOUND;;
            if (postRepository.existsById(id)) {
                Post post = postRepository.findById(id).get();
                Collection<Comment> comments = post.getComments();
                comments.forEach(c-> commentRepository.deleteById(c.getId()));
                commentRepository.flush();
                // for (Comment comment : comments) {
                //     comment.getUserAccount().getComments().remove(comment);
                // }    
                // comments.clear();  
                // post.getUserAccount().getPosts().remove(post);
                postRepository.deleteById(id);
                postRepository.flush();
                System.out.println("Deleted post with id: " + id);
                message = "SUCCESS";
                response = HttpStatus.OK;
            } else {
                System.out.println("Could not find post with id: " + id);
            }
            return new ResponseEntity<>(message, response);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception thrown: id was null.");
        } catch (ClassCastException e2) {
            System.out.println("Exception thrown: failed to remove post or comment.");
        }
        return new ResponseEntity<>("FAIL", HttpStatus.NOT_FOUND);
    }
}
