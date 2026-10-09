package ru.kata.spring.boot_security.demo.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<User> findAll() {
        return entityManager.createQuery("FROM User u LEFT JOIN FETCH u.roles", User.class).getResultList();
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return entityManager.createQuery("FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }
    
    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }
    
    @Override
    public void save(User user) {
        entityManager.persist(user);
    }
    
    @Override
    public void update(User user) {
        entityManager.merge(user);
    }
    
    @Override
    public void delete(Long id) {
        User user = getUserById(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
