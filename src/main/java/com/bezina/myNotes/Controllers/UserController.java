package com.bezina.myNotes.Controllers;

import com.bezina.myNotes.Entities.User;
import com.bezina.myNotes.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/user",
                produces = "application/json")
public class UserController {

 @Autowired
 UserService userService;
    @GetMapping
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
      return userService.getUserById(id);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user){
      return  userService.addUser(user);
    }
    @PutMapping(path = "/{id}", consumes = "application/json")
    public User putUser(
            @PathVariable("id") Long id,
            @RequestBody User user){
        return userService.putUser(id, user);
    }
    @PatchMapping(path = "/{id}",
            consumes = "application/json")
    public User patchUser(@PathVariable("id") Long id,
                            @RequestBody User userPatch){
      return userService.patchUser(id, userPatch);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser (@PathVariable Long id){
       userService.deleteUser(id);
    }
}
