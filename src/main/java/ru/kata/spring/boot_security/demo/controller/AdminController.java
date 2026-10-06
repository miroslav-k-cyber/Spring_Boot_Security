package ru.kata.spring.boot_security.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    
    public AdminController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping
    public String showAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
    
    @GetMapping("/new")
    public String newUserForm(@ModelAttribute("user") User user) {
        return "new-user";
    }
    
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
    
    @GetMapping("/edit")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit-user";
    }
    
    @PostMapping
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(value = "roles", required = false) List<Long> roleIds) {
        Set<Role> rolesSet = new HashSet<>();
        if (roleIds != null) {
            for (Long id : roleIds) {
                rolesSet.add(new Role(id, id == 1L ? "ROLE_ADMIN" : "ROLE_USER"));
            }
        }
        user.setRoles(rolesSet);
        userService.saveUser(user);
        return "redirect:/admin";
    }
    
    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "roles", required = false) List<Long> roleIds) {
        Set<Role> rolesSet = new HashSet<>();
        if (roleIds != null) {
            for (Long id : roleIds) {
                rolesSet.add(new Role(id, id == 1L ? "ROLE_ADMIN" : "ROLE_USER"));
            }
        }
        user.setRoles(rolesSet); // Привязываем созданный сет ролей к объекту
        userService.updateUser(user);
        return "redirect:/admin";
    }


}




