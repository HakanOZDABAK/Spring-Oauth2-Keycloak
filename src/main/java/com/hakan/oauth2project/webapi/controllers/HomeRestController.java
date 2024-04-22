package com.hakan.oauth2project.webapi.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = "*")
public class HomeRestController {



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String home(){
        return "Hello";
    }
}
