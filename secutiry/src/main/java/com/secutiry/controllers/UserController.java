package com.secutiry.controllers;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.secutiry.entities.Role;
import com.secutiry.entities.User;
import com.secutiry.repositories.UserRepository;
import com.secutiry.services.UserService;

@RestController
@RequestMapping(value = "api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Object> getAllUsers(){
        try{
            return ResponseEntity.ok().body(userRepository.findAll()) ;
        } catch (Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") UUID id){
        try{
            return ResponseEntity.ok().body(userRepository.findById(id)) ;
        } catch (Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> insertUser(@RequestBody User user){
        try{
            return ResponseEntity.ok().body(userRepository.save(user));
        }catch(Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id){
        try{
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @PostMapping("/{id}/add-role")
    public ResponseEntity<Object> addRoleToUser(@RequestBody Role role, @PathVariable("id") UUID userId){
        try{
            userService.addRoleToUser(role.getName(), userId);
            return ResponseEntity.ok().build();

        } catch(NotFoundException ex){
            return ResponseEntity.badRequest().body("User not found");

        } catch(DuplicateKeyException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());

        } catch(Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

}
