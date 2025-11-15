package com.fairshare.fairshare.service;

import com.fairshare.fairshare.Model.User;
import com.fairshare.fairshare.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public User register(String name, String email, String rawPassword) {
        if (repo.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        User u = User.builder()
                .name(name)
                .email(email)
                .password(encoder.encode(rawPassword))
                .role("ROLE_USER")
                .build();
        return repo.save(u);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getPassword(),
                List.of(new SimpleGrantedAuthority(u.getRole()))
        );
    }
}