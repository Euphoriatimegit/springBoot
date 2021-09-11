package org.example.controllers;

import org.example.model.Role;
import org.example.model.User;
import org.example.service.RoleService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UsersController {

    private UserService userService;
    private RoleService roleService;

    public UsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "users/login";
    }

    @GetMapping("/user")
    public String info(Model model, Principal principal) {
        User user = userService.findByLogin(principal.getName());
        model.addAttribute("user", user);
        return "users/info";
    }

    @GetMapping("/admin")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("roles", roleService.getAllRoles());
        return "users/index";
    }

    @GetMapping("/admin/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "users/show";
    }

    @GetMapping("admin/new")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "users/new";
    }

    @PostMapping("/admin")
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam(value = "addRoles", required = false) String[] addRoles,
                         Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        if (bindingResult.hasErrors() || addRoles == null) {
            return "users/new";
        }
        user.setRoles(convertorToRoles(addRoles));
        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        user.setPassword(null);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "users/edit";
    }

    @PutMapping("/admin/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id,
                         @RequestParam(value = "editRoles", required = false) String[] editRoles,
                         Model model) {
        if (bindingResult.hasErrors()||editRoles==null) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/edit";
        }
        user.setRoles(convertorToRoles(editRoles));
        userService.update(user);
        return "redirect:/admin";
    }

    private Set<Role> convertorToRoles(String... roles) {
        Set<Role> result = new HashSet<>();
        for (String role : roles) {
            result.add(roleService.findByName(role));
        }
        return result;
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

}
