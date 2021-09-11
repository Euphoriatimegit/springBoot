package org.example;

import org.example.model.Role;
import org.example.model.User;
import org.example.service.RoleService;
import org.example.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Start {

    private UserService userService;
    private RoleService roleService;

    public Start(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Bean
    public void init() {
        Role role_admin = new Role("ROLE_ADMIN");
        Role role_user = new Role("ROLE_USER");
        roleService.creatRole(role_admin);
        roleService.creatRole(role_user);
        userService.save(new User("Tom", (byte) 30, "Tom@mail.ru", "admin", "123", role_user, role_admin));
        userService.save(new User("Den", (byte) 33, "Den@mail.ru", "user", "123", role_user));
    }
}
