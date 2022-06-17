package com.secutiry.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.secutiry.entities.Role;
import com.secutiry.entities.User;
import com.secutiry.repositories.RoleRepository;
import com.secutiry.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void addRoleToUser(String roleName, UUID userId) throws NotFoundException{

        Optional<User> optionalUser = userRepository.findById(userId);
        Role role = roleRepository.findByName(roleName);

        if (optionalUser.isEmpty())
            throw new NotFoundException();
        
        User user = optionalUser.get();

        if (user.getRoles().contains(role))
            throw new DuplicateKeyException("User already have this role");

        user.getRoles().add(role);
        userRepository.save(user);
    }
    
}
