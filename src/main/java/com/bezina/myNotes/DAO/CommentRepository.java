package com.bezina.myNotes.DAO;

import com.bezina.myNotes.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    Iterable<Comment> findAllCommentsByUserId(Long userID);
    Iterable<Comment> findAllCommentsByNoteId(Long noteID);
}
