package com.fairshare.fairshare.web;

import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.security.AuthUtil;
import com.fairshare.fairshare.service.UserService;
import com.fairshare.fairshare.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final AuthUtil authUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterRequest req) {
        return ResponseEntity.ok(userService.register(req.getName(), req.getEmail(), req.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        String token = authUtil.createToken(req.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}