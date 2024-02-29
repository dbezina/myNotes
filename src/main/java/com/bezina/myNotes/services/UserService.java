package com.bezina.myNotes.services;

import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.DTO.UserDTO;
import com.bezina.myNotes.entities.User;
import com.bezina.myNotes.entities.enums.ERole;
import com.bezina.myNotes.exceptions.UserExistException;
import com.bezina.myNotes.payload.request.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
        user.setLogin(userIn.getUsername());
        user.setName(userIn.getName());
        user.setLastname(userIn.getLastname());
        user.setEmail(userIn.getEmail());
        user.setBio(userIn.getBio());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);
        user.setEnabled(1);
        try {
            LOG.info("Saving user {}" + userIn.getEmail());
            return userRepo.save(user);
        } catch (Exception e) {
            LOG.error("Something went wrong {}", e.getMessage());
            throw new UserExistException("The user {}" + user.getEmail() + "is already exists. Please check the credentials");
        }

    }

  /*  UserService(UserRepository userRepository){
        userRepo = userRepository;
    }*/

    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }


    public User getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent())
            return user.get();
        else return null;
    }

    public User getUserByName(String username) {
        Optional<User> user = userRepo.findUserByLogin(username);
        if (user.isPresent())
            return user.get();
        else return null;
    }

    public User addUser(User user) {
        return userRepo.save(user);
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
      //  User user = getUserByPrincipal(principal);
        User user = ServiceFunctions.getUserByPrincipal(userRepo, principal);
        user.setName(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());

        return userRepo.save(user);
    }

    public User getCurrentUser(Principal principal) {
      //  return getUserByPrincipal(principal);
        return  ServiceFunctions.getUserByPrincipal(userRepo, principal);
    }

  /*  private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        if (userRepo.findUserByLogin(username).isPresent())
            return userRepo.findUserByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
        else return null;
    }*/

    public User putUser(Long id, User user) {
        user.setId(id);
        return userRepo.save(user);
    }

    public User patchUser(Long id, User userPatch) {
        User user = userRepo.findById(id).get();
        if (userPatch.getName() != null) {
            user.setName(userPatch.getName());
        }
        if (userPatch.getLogin() != null) {
            user.setLogin(userPatch.getLogin());
        }
        if (userPatch.getBio() != null) {
            user.setBio(userPatch.getBio());
        }
        if (userPatch.getLastname() != null) {
            user.setLastname(userPatch.getLastname());
        }
        if (userPatch.getEmail() != null) {
            user.setEmail(userPatch.getEmail());
        }
        if (userPatch.getRoles() != null) {
            user.setRoles(userPatch.getRoles());
        }
        if (userPatch.getRate() != 0) {
            user.setRate(userPatch.getRate());
        }
        return userRepo.save(user);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent())
            userRepo.deleteById(id);
    }

}
