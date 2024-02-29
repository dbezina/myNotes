package com.bezina.myNotes.services;

import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.entities.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;

public class ServiceFunctions {
    public static User getUserByPrincipal(UserRepository userRepository, Principal principal){
            String username = principal.getName();
            if (userRepository.findUserByLogin(username).isPresent())
                return userRepository.findUserByLogin(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
            else return null;
    }
}
