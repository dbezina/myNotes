package com.bezina.myNotes.DAO;

import com.bezina.myNotes.Entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Iterable<Note> findAllNotesByUserId(Long userId);
}
