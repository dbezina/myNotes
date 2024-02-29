package com.bezina.myNotes.facade;

import com.bezina.myNotes.DTO.NoteDTO;
import com.bezina.myNotes.entities.Note;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NoteFacade {
    public Logger LOG = LoggerFactory.getLogger(NoteFacade.class);
    public NoteDTO noteToNoteDTO(Note note){
    //    LOG.info("noteToNoteDTO");
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(note.getId());
   //     LOG.info("id = "+noteDTO.getId().toString());
        noteDTO.setTitle(note.getTitle());
        noteDTO.setMemo(note.getMemo());
        noteDTO.setUsername(note.getUser().getLogin());
        return noteDTO;
    }
}
