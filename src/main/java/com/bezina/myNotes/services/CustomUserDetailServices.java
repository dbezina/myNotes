package com.bezina.myNotes.services;

import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailServices implements UserDetailsService {
    private final UserRepository userRepo;
    @Autowired
    public CustomUserDetailServices(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(username).
                orElseThrow(() -> new UsernameNotFoundException("the user with email "+ username+ " not found") );
        return build(user);
    }
    public User findUserById(Long id){
        return userRepo.findUserById(id).orElse(null);
    }

    public static User build(User user){
        List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        return  new User(user.getId(),user.getLogin(), user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
