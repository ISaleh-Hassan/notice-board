package experis.noticeboard.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import experis.noticeboard.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
}
