package com.bezina.myNotes.controllers;

import com.bezina.myNotes.DTO.NoteDTO;
import com.bezina.myNotes.entities.Note;
import com.bezina.myNotes.facade.NoteFacade;
import com.bezina.myNotes.payload.response.MessageResponse;
import com.bezina.myNotes.services.NoteService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "api/note",
produces = "application/json")
public class NoteController {
    public final Logger LOG = LoggerFactory.getLogger(NoteController.class);
    @Autowired   NoteService noteService;
    @Autowired
    private NoteFacade noteFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping
    public Iterable<Note> getAllMyNotes(){
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

    @PostMapping("/create")
    public ResponseEntity<Object> createNote(@Valid @RequestBody NoteDTO noteDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Note note = noteService.createNote(noteDTO, principal);
        NoteDTO createdNote = noteFacade.noteToNoteDTO(note);

        return new ResponseEntity<>(createdNote, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NoteDTO>> getAllNotes() {
        LOG.info("getAllNotes");
        Stream<Note> noteStream = StreamSupport.stream( noteService.getAllNotes().spliterator(),false);
        LOG.info(noteStream.toString());
        List<NoteDTO> noteDTOList = noteStream
                .filter(note -> note.getUser()!= null)
                .map(noteFacade::noteToNoteDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(noteDTOList, HttpStatus.OK);
    }

    @GetMapping("/user/notes")
    public ResponseEntity<List<NoteDTO>> getAllNotesForUser(Principal principal) {
        Stream<Note> noteStream = StreamSupport.stream(noteService.getAllNotesByUser(principal).spliterator(), false);
        List<NoteDTO> noteDTOList = noteStream
                .filter(note -> note.getUser()!= null)
                .map(noteFacade::noteToNoteDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(noteDTOList, HttpStatus.OK);
    }


    @PostMapping("/{noteId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("noteId") String noteId, Principal principal) {
        noteService.deleteNote(Long.parseLong(noteId), principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
