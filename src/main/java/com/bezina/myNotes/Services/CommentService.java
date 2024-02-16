package com.bezina.myNotes.Services;

import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.DAO.CommentRepository;
import com.bezina.myNotes.DAO.NoteRepository;
import com.bezina.myNotes.Entities.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    UserRepository authorRepo;
    NoteRepository noteRepo;
    CommentRepository commentRepo;

    @Autowired
    CommentService(UserRepository userRepository, NoteRepository noteRepository,
                   CommentRepository commentRepository) {
        authorRepo = userRepository;
        noteRepo = noteRepository;
        commentRepo = commentRepository;
    }

    public Iterable<Comment> getAllComments() {
        return commentRepo.findAll();
    }

    public Iterable<Comment> getAllCommentsByUser(Long authorId) {
        try {
            return commentRepo.findAllCommentsByUserId(authorId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public Iterable<Comment> getAllCommentsByNote(Long noteId) {
        try {
            return commentRepo.findAllCommentsByNoteId(noteId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Comment getCommentByID(Long id) {
        Optional<Comment> comment = commentRepo.findById(id);
        return comment.orElse(null);
    }

    public Comment addComment(Comment comment) {
        return commentRepo.save(comment);
    }

    public Comment patchComment(Long id, Comment commentPatch) {
        Comment comment = commentRepo.findById(id).orElse(null);
        if (commentPatch.getNote() != null){
            comment.setNote(commentPatch.getNote());
        }
        if (commentPatch.getText() != null){
            comment.setText(commentPatch.getText());
        }
        if (commentPatch.getUser() != null) {
            comment.setUser(commentPatch.getUser());
        }
        return commentRepo.save(comment);
    }
    public void deleteComment(Long id){
        Optional<Comment> deletedComment = commentRepo.findById(id);
        if(deletedComment.isPresent())
            commentRepo.deleteById(id);
    }
}
