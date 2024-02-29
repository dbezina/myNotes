package com.bezina.myNotes.services;

import com.bezina.myNotes.DAO.ImageRepository;
import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.DAO.NoteRepository;
import com.bezina.myNotes.DTO.NoteDTO;
import com.bezina.myNotes.entities.ImageModel;
import com.bezina.myNotes.entities.Note;
import com.bezina.myNotes.entities.User;
import com.bezina.myNotes.exceptions.NoteNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.method.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;


@Service
public class NoteService {
    public static final Logger LOG = (Logger) LoggerFactory.getLogger(NoteService.class);

    private final UserRepository userRepo;
    private final NoteRepository noteRepo;
    private final ImageRepository imageRepo;

    @Autowired
    NoteService(UserRepository userRepository, NoteRepository noteRepository, ImageRepository imageRepository) {
        userRepo = userRepository;
        noteRepo = noteRepository;
        imageRepo = imageRepository;
    }

    public Iterable<Note> getAllNotes() {
         return noteRepo.findAll();
      //  return noteRepo.findAllByOrderByCreatedDateDesc();
    }

    public Iterable<Note> getAllNotesByUser(Long userId) {
        try {
            return noteRepo.findAllNotesByUserId(userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Iterable<Note> getAllNotesByUser(Principal principal) {
        User user = userRepo.findUserByLogin(principal.getName()).get();
        return getAllNotesByUser(user.getId());

    }

    public Note getNoteByIDAndUser(Long id, Principal principal) {
        User user = userRepo.findUserByLogin(principal.getName()).get();
        Optional<Note> note = noteRepo.findNoteByIdAndUser(id, user);
        return note.orElseThrow(() -> new NoteNotFoundException("Note can't be found for user: {}" + user.getEmail()));

    }

    public Note getNoteByID(Long id) {
        Optional<Note> note = noteRepo.findById(id);
        return note.orElseThrow(() ->
                new EmptyResultDataAccessException("note with id: {} not found" + id, 1000));
    }

    public Note createNote(NoteDTO noteDTO, Principal principal) {
        Note createdNote = new Note();
        User createByUser =  ServiceFunctions.getUserByPrincipal(userRepo, principal);
                //getUserByPrincipal(principal);
        createdNote.setUser(createByUser);
        createdNote.setMemo(noteDTO.getMemo());
        createdNote.setTitle(noteDTO.getTitle());
        LOG.info("Saving Post for User: {}", createByUser.getEmail());
        return  noteRepo.save(createdNote);
    }


    public Note addNote(Note note) {
        return noteRepo.save(note);
    }

    public Note patchNote(Long id, Note notePatch) {
        Note note = noteRepo.findById(id).orElse(null);
        if (notePatch.getMemo() != null) {
            note.setMemo(notePatch.getMemo());
        }
        if (notePatch.getTitle() != null) {
            note.setTitle(notePatch.getTitle());
        }
        if (notePatch.getUser() != null) {
            note.setUser(notePatch.getUser());
        }
        return noteRepo.save(note);
    }

    public void deleteNote(Long id) {
        Optional<Note> deletedNote = noteRepo.findById(id);
        if (deletedNote.isPresent())
            noteRepo.deleteById(id);
    }
    public void deleteNote(Long id, Principal principal){
       // User user = userRepo.findUserByLogin(principal.getName()).get();
        Note note = getNoteByIDAndUser(id, principal);
        Optional<ImageModel> image = imageRepo.findImageModelByNoteId(note.getId());
        image.ifPresent(imageRepo::delete);
        noteRepo.delete(note);

    }
}
