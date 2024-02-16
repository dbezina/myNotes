package com.bezina.myNotes.Services;

import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.DAO.NoteRepository;
import com.bezina.myNotes.Entities.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NoteService {
    UserRepository authorRepo;
    NoteRepository noteRepo;

    @Autowired
    NoteService(UserRepository userRepository, NoteRepository noteRepository) {
        authorRepo = userRepository;
        noteRepo = noteRepository;
    }

    public Iterable<Note> getAllNotes() {
        return noteRepo.findAll();
    }

    public Iterable<Note> getAllNotesByUser(Long userId) {
        try {
            return noteRepo.findAllNotesByUserId(userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Note getNoteByID(Long id) {
        Optional<Note> note = noteRepo.findById(id);
        return note.orElse(null);
    }

    public Note addNote(Note note) {
        return noteRepo.save(note);
    }

    public Note patchNote(Long id, Note notePatch) {
        Note note = noteRepo.findById(id).orElse(null);
        if (notePatch.getMemo() != null){
            note.setMemo(notePatch.getMemo());
        }
        if (notePatch.getTitle() != null){
            note.setTitle(notePatch.getTitle());
        }
        if (notePatch.getUser() != null) {
            note.setUser(notePatch.getUser());
        }
        return noteRepo.save(note);
    }
    public void deleteNote(Long id){
        Optional<Note> deletedNote = noteRepo.findById(id);
        if (deletedNote.isPresent())
            noteRepo.deleteById(id);
    }
}
