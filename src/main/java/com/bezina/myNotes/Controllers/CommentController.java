package com.bezina.myNotes.Controllers;

import com.bezina.myNotes.Entities.Comment;
import com.bezina.myNotes.Entities.Note;
import com.bezina.myNotes.Services.CommentService;
import com.bezina.myNotes.Services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/comment",
produces = "application/json")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping
    Iterable<Comment> getAllNotes(){
        return commentService.getAllComments();
            }
    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Long id){
        return commentService.getCommentByID(id);
    }
    @GetMapping("/byUser/{userId}")
    Iterable<Comment> gelAllCommentsByAuthorId(@PathVariable Long userId){
        return commentService.getAllCommentsByUser(userId);
    }
    @GetMapping("/byNote/{noteId}")
    Iterable<Comment> gelAllCommentsByNoteId(@PathVariable Long noteId){
        return commentService.getAllCommentsByNote(noteId);
    }
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    Comment addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
    @PatchMapping(path = "/{id}",
            consumes = "application/json")
    public Comment patchComment(@PathVariable("id") Long id,
                              @RequestBody Comment commentPatch){
        return commentService.patchComment(id,commentPatch);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable ("id") Long id){
        commentService.deleteComment(id);
    }
}
