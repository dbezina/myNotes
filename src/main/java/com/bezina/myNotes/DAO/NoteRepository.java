package com.bezina.myNotes.DAO;

import com.bezina.myNotes.entities.Note;
import com.bezina.myNotes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Iterable<Note> findAllNotesByUserId(Long userId);
    Iterable<Note> findAllByOrderByCreatedDateDesc();
    Optional<Note> findNoteByIdAndUser(Long id, User user);
    Optional<Note> findNoteById(Long id);
}
