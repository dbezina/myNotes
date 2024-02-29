package com.bezina.myNotes.controllers;

import com.bezina.myNotes.DTO.CommentDTO;
import com.bezina.myNotes.entities.Comment;
import com.bezina.myNotes.facade.CommentFacade;
import com.bezina.myNotes.payload.response.MessageResponse;
import com.bezina.myNotes.services.CommentService;
import com.bezina.myNotes.validators.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "api/comment",
        produces = "application/json")
public class CommentController {
    public final Logger LOG = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;
    @Autowired
    private CommentFacade commentFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping
    Iterable<Comment> getAllNotes() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Long id) {
        return commentService.getCommentByID(id);
    }

    @GetMapping("/byUser/{userId}")
    Iterable<Comment> gelAllCommentsByAuthorId(@PathVariable Long userId) {
        return commentService.getAllCommentsByUser(userId);
    }

    @GetMapping("/byNote/{noteId}")
    Iterable<Comment> gelAllCommentsByNoteId(@PathVariable Long noteId) {
        return commentService.getAllCommentsByNote(noteId);
    }

    @GetMapping("/{noteId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToNote(@PathVariable("noteId") String noteId) {
        Stream<Comment> commentStream =
                StreamSupport.stream(commentService.getAllCommentsByNote(Long.parseLong(noteId)).spliterator(), false);
        List<CommentDTO> commentDTOList = commentStream
                .map(commentFacade::commentToCommentDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Comment addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @PatchMapping(path = "/{id}",
            consumes = "application/json")
    public Comment patchComment(@PathVariable("id") Long id,
                                @RequestBody Comment commentPatch) {
        return commentService.patchComment(id, commentPatch);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Comment id = {} was deleted" + commentId), HttpStatus.OK);
    }

    @PostMapping("/{noteId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                                @PathVariable("noteId") String noteId,
                                                BindingResult bindingResult,
                                                Principal principal) {
        //   LOG.info("createComment");
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        //     LOG.info("no errors");
        Comment comment = commentService.saveComment(Long.parseLong(noteId), commentDTO, principal);
        //      LOG.info("comment "+ comment.getText());
        CommentDTO createdComment = commentFacade.commentToCommentDTO(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

}
