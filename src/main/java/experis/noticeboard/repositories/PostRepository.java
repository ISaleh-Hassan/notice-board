package experis.noticeboard.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import experis.noticeboard.models.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
    
}
