package experis.noticeboard.models;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.Collection;


@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserAccount {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String userName;
    
    @Column
    private String password;

    @Column
    public boolean active;

    @Column
    public String roles;
   
    @OneToMany(mappedBy="userAccount", fetch=FetchType.EAGER) 
    private Collection<Post> posts = new ArrayList<Post>();

    @OneToMany(mappedBy="userAccount", fetch=FetchType.EAGER)
    private Collection<Comment> comments = new ArrayList<Comment>();

    public UserAccount() {
        
    }

    public UserAccount(Integer id) {
        this.id = id;
    }

    public UserAccount(String username, String password, boolean active, String role) {
        this.userName = username;
        this.password = password;
        this.active = active;
        this.roles = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Post> getPosts() {
        return posts;
    }

    public void setPosts(Collection<Post> posts) {
        this.posts = posts;
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

}

