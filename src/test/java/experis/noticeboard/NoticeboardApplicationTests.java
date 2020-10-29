package experis.noticeboard;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import experis.noticeboard.controllers.api.CommentController;
import experis.noticeboard.controllers.api.PostController;
import experis.noticeboard.controllers.api.UserAccountController;
import experis.noticeboard.models.Comment;
import experis.noticeboard.models.Post;
import experis.noticeboard.models.UserAccount;
import experis.noticeboard.repositories.CommentRepository;
import experis.noticeboard.repositories.PostRepository;
import experis.noticeboard.repositories.UserAccountRepository;

@SpringBootTest
class NoticeboardApplicationTests {

    @Autowired
    private UserAccountController uac; 

    @Autowired
    private PostController pc;

    @Autowired
    private CommentController cc;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserAccountRepository uaRepository;

    @Test
    void getOneUserFromId() {
        int id = createATestPerson();
        ResponseEntity<UserAccount> response = uac.getUserById(id); 
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllUsers() {
        ResponseEntity<ArrayList<UserAccount>> response = uac.getAllUsers();   
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createUsers() {
        ResponseEntity<UserAccount> response = uac.addUser(new UserAccount("TestPerson", "TestPassword"));        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("TestPerson", response.getBody().getUsername());
        assertEquals("TestPassword", response.getBody().getPassword());
    }

    @Test
    void updateUser() {
        int id = createATestPerson();        
        ResponseEntity<UserAccount> response = uac.updateUser(new UserAccount("UpdatedUser", "UpdatedPassword"), id);   
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UpdatedUser", response.getBody().getUsername());
        assertEquals("UpdatedPassword", response.getBody().getPassword());
    }

    @Test
    void deleteUser() {
        int id = createATestPerson(); 
        ResponseEntity<UserAccount> response2 = uac.getUserById(id);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        ResponseEntity<String> response = uac.deleteUser(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        response2 = uac.getUserById(id);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    }

    @Test
    void getOnePostFromId() {
        int userId = createATestPerson();
        int postId = createATestPost(userId);
        ResponseEntity<Post> response = pc.getPostById(postId); 
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllPosts() {
        ResponseEntity<ArrayList<Post>> response = pc.getAllPosts();   
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createPost() {
        int userId = createATestPerson();
        ResponseEntity<Post> response = pc.addPost(new Post("This is a test post", uac.getUserById(userId).getBody()), userId);        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("This is a test post", response.getBody().getMessage());
    }

    @Test
    void updatePost() {
        int userId = createATestPerson();
        int postId = createATestPost(userId);        
        ResponseEntity<Post> response = pc.updatePost(new Post("This is a updated post", uac.getUserById(userId).getBody()), postId);   
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This is a updated post", response.getBody().getMessage());
    }

    @Test
    void deletePost() {
        int userId = createATestPerson();
        int postId = createATestPost(userId); 
        ResponseEntity<Post> response2 = pc.getPostById(postId);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        System.out.println(userId);
        System.out.println(postId);
        ResponseEntity<String> response = pc.deletePost(postId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        response2 = pc.getPostById(postId);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    }

    @Test
    void deletePostWithComments() {
        int userId = createATestPerson();
        int postId = createATestPost(userId); 
        ResponseEntity<Post> response2 = pc.getPostById(postId);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        int commentId = createATestComment(userId, postId);
        System.out.println("!!!!!!!!!!!!!!!!!! " + postId + " " + userId + " " + commentId +"!!!!!!!!!!!!!!!!!!!!!");
        ResponseEntity<String> response = pc.deletePost(postId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        response2 = pc.getPostById(postId);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        // UserAccount userAccount = new UserAccount();
        // UserAccount savedUa = uaRepository.save(userAccount);

        // Post post = new Post();
        // post.setUserAccount(new UserAccount(savedUa.getId()));
        // Post savedPost = postRepository.save(post);

        // Comment c = new Comment();
        // c.setMessage("Hejsan");
        // c.setUserAccount(new UserAccount(savedUa.getId()));
        // c.setPost(new Post(savedPost.getId()));

        // assertEquals(0, commentRepository.count());
        // Comment saved = commentRepository.save(c);
        // assertEquals(1, commentRepository.count());

        // Post p1 = postRepository.findById(savedPost.getId()).get();
        // assertEquals(1, p1.getComments().size());

        // commentRepository.deleteById(saved.getId());
        // assertEquals(0, commentRepository.count());

        // Post p2 = postRepository.findById(savedPost.getId()).get();
        // assertEquals(0, p2.getComments().size());

        // UserAccount u = uaRepository.findById(savedUa.getId()).get();
        // assertEquals(0, u.getComments().size());
        // assertEquals(1, u.getPosts().size());
    }


    int createATestPerson() {
        ResponseEntity<UserAccount> response = uac.addUser(new UserAccount("TestPerson", "testPassword"));
        return response.getBody().getId();
    }

    int createATestPost(int userId) {
        ResponseEntity<Post> response2 = pc.addPost(new Post("This is a test post", null), userId);
        return response2.getBody().getId();
    }

    int createATestComment(int userId, int postId) {
        ResponseEntity<Comment> response = cc.addComment(new Comment("This is a test comment", null, null), userId, postId);
        return response.getBody().getId();
    }

}
