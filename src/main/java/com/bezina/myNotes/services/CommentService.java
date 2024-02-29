package com.bezina.myNotes.services;

import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.DAO.CommentRepository;
import com.bezina.myNotes.DAO.NoteRepository;
import com.bezina.myNotes.DTO.CommentDTO;
import com.bezina.myNotes.entities.Comment;
import com.bezina.myNotes.entities.Note;
import com.bezina.myNotes.entities.User;
import com.bezina.myNotes.exceptions.NoteNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class CommentService {
    public static final Logger LOG = (Logger) LoggerFactory.getLogger(CommentService.class);

    private final UserRepository userRepo;
    private final NoteRepository noteRepo;
    private final CommentRepository commentRepo;

    @Autowired
    CommentService(UserRepository userRepository, NoteRepository noteRepository,
                   CommentRepository commentRepository) {
        userRepo = userRepository;
        noteRepo = noteRepository;
        commentRepo = commentRepository;
    }
  /*  private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        if (userRepo.findUserByLogin(username).isPresent())
            return userRepo.findUserByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
        else return null;
    }*/

    public Comment saveComment(Long noteID, CommentDTO commentDTO, Principal principal){
    //    User user = getUserByPrincipal(principal);
        User user = ServiceFunctions.getUserByPrincipal(userRepo, principal);
        Note note = noteRepo.findNoteByIdAndUser(noteID, user)
                .orElseThrow(()-> new NoteNotFoundException("Note was not found for user {}"+user.getEmail()));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setNote(note);
        comment.setText(commentDTO.getText());
        LOG.info("comment for Note with id {} is saved"+note.getId());
        return commentRepo.save(comment);

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
    public Iterable<Comment> getAllCommentsByNote(Note note){
        Note searchNote = noteRepo.findNoteById(note.getId())
                .orElseThrow(()-> new NoteNotFoundException("Note with id {} not found"+note.getId()));
        return getAllCommentsByNote(searchNote.getId());

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
        deletedComment.ifPresent(commentRepo::delete);
    }
}
