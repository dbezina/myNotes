package com.bezina.myNotes.payload.request;

import com.bezina.myNotes.annotations.PasswordMatchers;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@PasswordMatchers
public class SignupRequest {
    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "LastName is required")
    private String lastname;
    @Email(message = "Enter Email in correct way")
    @NotBlank(message = "User email is required")
  //  @ValidEmail
    private String email;
    private String bio;
    @NotEmpty(message = "Username is required")
    private String username;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$",
            message = "Пароль должен содержать минимум одну цифру, одну букву и иметь длину не менее 6 символов")
    private String password;
    private String confirmPassword;

}
