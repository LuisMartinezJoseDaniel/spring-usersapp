package com.danny.backend.usersapp.controllers;

import com.danny.backend.usersapp.models.entities.User;
import com.danny.backend.usersapp.models.request.UserRequest;
import com.danny.backend.usersapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("users")
@CrossOrigin("http://localhost:5173")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<User> list() {
        return this.service.findAll();
    }

    @GetMapping("/{id}") // Path Variable
    public ResponseEntity<?> show(@PathVariable(name = "id") Long id) {
        Optional<User> userOptional = this.service.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }

        return ResponseEntity.ok(userOptional.orElseThrow());//200
    }


    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return this.validation(result);
        }
        // User viene en el request
        // Se retorna el status 201 y el body como JSON de user
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest user, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return this.validation(result);
        }

        Optional<User> userOptional = this.service.update(user, id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.orElseThrow());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id) {
        Optional<User> userOptional = this.service.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();//404
        }

        this.service.remove(id);
        return ResponseEntity.noContent().build();//204
    }

    //Metodo para validar los campos de User
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>(); // Similar a un objeto de JS
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors); //Se convierte en JSON
    }


//    @PostMapping // 1era forma
//    @ResponseStatus(HttpStatus.CREATED)
//    public User store(@RequestBody User user){
//        // User viene en el request
//        return this.service.save(user);
//    }


}
