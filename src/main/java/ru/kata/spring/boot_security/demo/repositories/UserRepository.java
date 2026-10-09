package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();
    
    Optional<User> findByEmail(String email);
    
    User getUserById(Long id);
    
    void save(User user);
    
    void update(User user);
    
    void delete(Long id);
}
