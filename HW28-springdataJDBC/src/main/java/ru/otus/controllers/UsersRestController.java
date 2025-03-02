package ru.otus.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.model.JsonResponse;
import ru.otus.model.RequestCreateUser;
import ru.otus.model.User;
import ru.otus.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {

    private final UserRepository userRepository;

    @Autowired
    public UsersRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            JsonResponse error = new JsonResponse("User not found", "No user exists with ID: " + id);
            return ResponseEntity.status(404).body(error);
        }

        return ResponseEntity.ok(user.orElse(null));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody RequestCreateUser request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            JsonResponse error = new JsonResponse("Invalid input", "User name cannot be empty");
            return ResponseEntity.status(400).body(error); // 400 Bad Request
        }

        User user = new User();
        user.setName(request.getName());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        User savedUser = userRepository.save(user);

        return ResponseEntity.status(201).body(savedUser);
    }
}
