package org.example.dao;

import org.example.model.User;

import java.util.List;

public interface UserDAO {

    List<User> getAllUser();

    User getUserById(Long id);

    void save(User user);

    void delete(Long id);

    User findByLogin(String login);

    void update(User user);
}
