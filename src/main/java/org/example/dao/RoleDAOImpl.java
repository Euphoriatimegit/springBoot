package org.example.dao;

import org.example.model.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDAOImpl implements RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    @Override
    public Role findByName(String name) {
        return entityManager.createQuery("SELECT r FROM Role r WHERE r.name=:name ", Role.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    @Transactional
    public void creatRole(Role role) {
        entityManager.persist(role);
    }
}
