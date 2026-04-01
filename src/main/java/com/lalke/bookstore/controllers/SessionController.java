package com.lalke.bookstore.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
    public ResponseEntity<Void> keepAlive(){
        return ResponseEntity.ok().build();
    }
}
