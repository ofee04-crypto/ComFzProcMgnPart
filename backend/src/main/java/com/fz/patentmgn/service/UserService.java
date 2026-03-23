package com.fz.patentmgn.service;

import com.fz.patentmgn.model.User;
import com.fz.patentmgn.repository.JsonUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final JsonUserRepository repository;
    public UserService(JsonUserRepository repository) { this.repository = repository; }

    public User authenticate(String username, String password) {
        Optional<User> userOpt = repository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        }
        return null;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }
    
    public User getUserByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }
    
    public void saveUser(User user) {
        repository.save(user);
    }
    
    public void deleteUser(String username) {
        if ("admin".equals(username)) return; // Prevent deleting default admin
        repository.deleteByUsername(username);
    }
}
