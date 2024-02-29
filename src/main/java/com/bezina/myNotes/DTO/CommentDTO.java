package com.bezina.myNotes.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    @NotEmpty
    private String text;
    private String username;

}
