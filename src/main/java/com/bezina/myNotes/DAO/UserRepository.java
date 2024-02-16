package com.bezina.myNotes.DAO;

import com.bezina.myNotes.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
