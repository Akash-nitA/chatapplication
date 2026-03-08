package com.example.chatApp.Services.Impl;

import com.example.chatApp.Models.Students;
import com.example.chatApp.Repository.AuthRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AuthRepo authrepo;
    UserDetailsServiceImpl(AuthRepo authrepo){
        this.authrepo=authrepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Students> student= authrepo.findOneByName(username);
        if(student.isEmpty()){
            throw new UsernameNotFoundException("UserName not found");
        }
        Students newStudent=student.get();
        return new User(newStudent.getName(), newStudent.getPassword(),new ArrayList<>());
    }
}
