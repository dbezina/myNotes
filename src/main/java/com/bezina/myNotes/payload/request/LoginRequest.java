package com.bezina.myNotes.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "username can't be empty")
    private String username;
    @NotEmpty(message = "password can't be empty")
    private String password;
}
