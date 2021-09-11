package org.example.service;

import org.example.model.Role;

import java.util.List;

public interface RoleService {
    void creatRole(Role role);
    List<Role> getAllRoles();
    Role findByName(String name);
}
