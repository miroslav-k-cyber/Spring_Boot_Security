package ru.kata.spring.boot_security.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService; // Общаемся с ролями только через их сервис
    
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }
    
    @Override
    @Transactional
    public void saveUser(User user, List<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            List<Role> roles = roleService.findRolesByIds(roleIds);
            user.setRoles(new HashSet<>(roles)); // Привязываем сет ролей внутри метода слоя User
        }
        userRepository.save(user);
    }
    
    @Override
    @Transactional
    public void updateUser(User user, List<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            List<Role> roles = roleService.findRolesByIds(roleIds);
            user.setRoles(new HashSet<>(roles)); // Привязываем сет ролей внутри метода слоя User
        }
        userRepository.update(user);
    }
    
    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
}
