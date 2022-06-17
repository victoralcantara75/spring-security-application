package com.secutiry.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secutiry.entities.Role;
import com.secutiry.repositories.RoleRepository;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<Object> getAllRoles(){
        try{
            return ResponseEntity.ok().body(roleRepository.findAll());
        } catch (Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> createRole(@RequestBody Role role){
        try{
            return ResponseEntity.ok().body(roleRepository.save(role));
        } catch (Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }
    
}
