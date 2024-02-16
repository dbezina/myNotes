package com.bezina.myNotes.Services;

import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    UserRepository userRepo;

    @Autowired
    UserService(UserRepository userRepository){
        userRepo = userRepository;
    }

    public Iterable<User> getAllUsers(){
        return userRepo.findAll();
    }


    public User getUserById(Long id){
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent())
            return user.get();
        else return null;
    }


    public User addUser(User user){
        return  userRepo.save(user);
    }
    public User putUser(Long id, User user){
        user.setId(id);
        return userRepo.save(user);
    }
    public User patchUser(Long id,User userPatch){
        User user = userRepo.findById(id).get();
        if (userPatch.getName() != null){
            user.setName(userPatch.getName());
        }
        if (userPatch.getLogin() != null){
            user.setLogin(userPatch.getLogin());
        }
        if (userPatch.getBio() != null){
            user.setBio(userPatch.getBio());
        }
        if (userPatch.getLastname() != null){
            user.setLastname(userPatch.getLastname());
        }
        if (userPatch.getEmail() != null){
            user.setEmail(userPatch.getEmail());
        }
        if (userPatch.getRoles() != null){
            user.setRoles(userPatch.getRoles());
        }
        if (userPatch.getRate() != 0) {
            user.setRate(userPatch.getRate());
        }
        return userRepo.save(user);
    }

    public void deleteUser (Long id){
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent())
            userRepo.deleteById(id);
    }

}
