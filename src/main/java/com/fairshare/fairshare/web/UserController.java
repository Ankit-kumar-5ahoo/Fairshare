package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.repo.UserRepository;
import com.fairshare.fairshare.web.dto.CreateUserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepo;
    public UserController(UserRepository userRepo){ this.userRepo = userRepo; }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid CreateUserRequest req){
        if (userRepo.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        User saved = userRepo.save(new User(req.getName(), req.getEmail()));
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<User> all(){ return userRepo.findAll(); }
}
