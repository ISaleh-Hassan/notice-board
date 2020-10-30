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


@SpringBootTest
class NoticeboardApplicationTests {

    @Autowired
    private UserAccountController uac; 

    @Autowired
    private PostController pc;

    @Autowired
    private CommentController cc;

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
        ResponseEntity<UserAccount> response = uac.addUser(new UserAccount("TestPerson", "TestPassword", false, "USER"));        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("TestPerson", response.getBody().getUserName());
        assertEquals("TestPassword", response.getBody().getPassword());
    }

    @Test
    void updateUser() {
        int id = createATestPerson();        
        ResponseEntity<UserAccount> response = uac.updateUser(new UserAccount("UpdatedUser", "UpdatedPassword", false, "USER"), id);   
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UpdatedUser", response.getBody().getUserName());
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
        createATestComment(userId, postId);
        ResponseEntity<String> response = pc.deletePost(postId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        response2 = pc.getPostById(postId);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    }


    int createATestPerson() {
        ResponseEntity<UserAccount> response = uac.addUser(new UserAccount("TestPerson", "testPassword", false, "USER"));
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
