package org.example.dao;

import org.example.model.Role;

import java.util.List;

public interface RoleDAO {
    void creatRole(Role role);
    List<Role> getAllRoles();
    Role findByName(String name);
}
