package com.bezina.myNotes.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class NoteDTO {
    private Long id;
    private String title;
    @NotEmpty
    private String memo;
    private String username;
   // private List<String> usersComments;
}
