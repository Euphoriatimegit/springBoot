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

    @GetMapping("/admin")
    public String getAllUsers(Model model, Principal principal) {
        model.addAttribute("user", userService.findByEmail(principal.getName()));
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("roles", roleService.getAllRoles());
        return "users/index";
    }

    @GetMapping("/admin/{id}")
    public String getUserInfoById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "show";
    }

    @GetMapping("/admin/new")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "users/new";
    }

    @PostMapping("/admin")
    public String newUserAdd(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         Model model,@RequestParam(value = "edit_roles") String[] edit_roles) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/new";
        }
        user.setRoles(convertorToRoles(edit_roles));
        userService.save(user);
        return "redirect:/admin";
    }

    @PatchMapping("/admin/{id}")
    public String update(@ModelAttribute("user") @Valid User user, Model model,
                         @RequestParam(value = "edit_roles") String[] edit_roles) {
        user.setRoles(convertorToRoles(edit_roles));
        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String UserInfo(Model model,Principal principal) {
        model.addAttribute("user", userService.findByEmail(principal.getName()));
        return "users/show";
    }

    private Set<Role> convertorToRoles(String... roles) {
        Set<Role> result = new HashSet<>();
        for (String role : roles) {
            result.add(roleService.findByName(role));
        }
        return result;
    }
}
