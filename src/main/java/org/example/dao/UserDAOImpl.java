package org.example.dao;

import org.example.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUser() {
        return entityManager.createQuery("SELECT u from User u",User.class).getResultList();
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.createQuery("SELECT u from User u LEFT join fetch u.roles where u.id=:id",User.class)
                .setParameter("id",id).getSingleResult();
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.merge(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        entityManager.createQuery("DELETE FROM User u WHERE u.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public User findByLogin(String login) {
        return entityManager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.login=:login ", User.class)
                .setParameter("login", login)
                .getSingleResult();
    }

    @Override
    @Transactional
    public void update(User user) {
        entityManager.merge(user);
    }
}
