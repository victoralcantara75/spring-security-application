package com.secutiry.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping {
    
    @GetMapping(value = "/api/ping")
    public ResponseEntity<Object> helloWorld(){
        return ResponseEntity.ok().body("Application is running!");
    }

}
