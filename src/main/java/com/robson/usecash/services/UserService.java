package com.robson.usecash.services;

import com.robson.usecash.domain.User;
import com.robson.usecash.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void saveUser(){
        User user = new User();
        user.setEmail("bobson278@gmail.com");
        user.setPassword("278279");
        System.out.println(user);
        userRepository.save(user);
    }
}
