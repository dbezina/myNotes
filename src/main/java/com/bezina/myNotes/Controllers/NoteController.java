package com.bezina.myNotes.Controllers;

import com.bezina.myNotes.Entities.Note;
import com.bezina.myNotes.Services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/note",
produces = "application/json")
public class NoteController {
    @Autowired   NoteService noteService;

    @GetMapping
    public Iterable<Note> getAllNotes(){
        return noteService.getAllNotes();
            }
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable Long id){
        return noteService.getNoteByID(id);
    }
    @GetMapping("/byUser/{userId}")
    public Iterable<Note> gelAllNotesByAuthorId(@PathVariable Long userId){
        return noteService.getAllNotesByUser(userId);
    }
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Note addNote(@RequestBody Note note){
        return noteService.addNote(note);
    }
    @PatchMapping(path = "/{id}",
            consumes = "application/json")
    public Note patchNote(@PathVariable("id") Long id,
                              @RequestBody Note notePatch){
        return noteService.patchNote(id,notePatch);
    }
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable("id") Long id){
        noteService.deleteNote(id);
    }
}
