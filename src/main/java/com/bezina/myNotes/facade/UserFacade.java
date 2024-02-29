package com.bezina.myNotes.facade;

import com.bezina.myNotes.DTO.UserDTO;
import com.bezina.myNotes.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user){
        UserDTO userDTO= new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getLogin());
        userDTO.setFirstname(user.getName());
        userDTO.setLastname(user.getLastname());
        userDTO.setBio(user.getBio());

        return userDTO;
    }
}
