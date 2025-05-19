package org.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import org.example.entity.User;
import org.example.security.PasswordHasher;
import org.example.exception.DuplicateUsernameException;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "web4PU")
    private EntityManager em;

    public User register(String username, String password) {
        if (findByUsername(username) != null) {
            throw new DuplicateUsernameException("Username already exists");
        }

        User user = new User(username, PasswordHasher.hash(password));
        em.persist(user);
        return user;
    }

    public User authenticate(String username, String password) {
        User user = findByUsername(username);
        if (user == null || !PasswordHasher.verify(password, user.getPassword())) {
            throw new DuplicateUsernameException("Invalid username or password");
        }
        return user;
    }

    public User findByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }
}