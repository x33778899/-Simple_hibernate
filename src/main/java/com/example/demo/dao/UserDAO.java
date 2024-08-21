package com.example.demo.dao;

import com.example.demo.model.Product;
import com.example.demo.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.util.List;

@Repository
public class UserDAO {
    private SessionFactory sessionFactory;

    public UserDAO() {
        sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();
    }

    public User saveUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
        return user;
    }

    public User getUser(Long id) {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class, id);
        session.close();
        return user;
    }

    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        List<User> users = session.createQuery("from User", User.class).list();
        session.close();
        return users;
    }

    public void updateUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        session.close();
    }

    public void deleteUser(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, id);
        if (user != null) {
            session.delete(user);
        }
        transaction.commit();
        session.close();
    }

    public User findByUsername(String username) {
        Session session = sessionFactory.openSession();

        String sql = "SELECT * FROM User WHERE username = ?";
        Query query = session.createNativeQuery(sql, User.class);
        query.setParameter(1, username);

        List<User> users = query.getResultList();
        session.close();

        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }
}