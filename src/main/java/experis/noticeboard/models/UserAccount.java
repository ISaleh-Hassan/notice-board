package experis.noticeboard.models;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    public String userName;
    public String password;
    public boolean active;
    public String roles;

    @OneToMany(orphanRemoval = true, cascade= CascadeType.ALL)
    private Collection<Post> posts = new ArrayList<Post>();

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Comment> comments = new ArrayList<Comment>();

    public int getId() {
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
}

