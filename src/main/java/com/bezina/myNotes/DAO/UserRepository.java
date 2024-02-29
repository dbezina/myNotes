package com.bezina.myNotes.DAO;

import com.bezina.myNotes.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User>  findUserByEmail(String email);
    Optional<User>  findUserById(Long id);
    Optional<User>  findUserByLogin(String username);
}
