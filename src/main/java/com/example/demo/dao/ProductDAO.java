package com.example.demo.dao;

import com.example.demo.model.Product;

import java.util.List;

import com.example.demo.model.Product;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAO {

    private SessionFactory sessionFactory;

    public ProductDAO() {
        // 使用 Hibernate 配置文件来配置 SessionFactory
        sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();
    }

    public Product saveProduct(Product product) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(product);
        transaction.commit();
        session.close();
        return product;
    }

    public Product getProduct(Long id) {
        Session session = sessionFactory.openSession();
        Product product = session.get(Product.class, id);
        session.close();
        return product;
    }

    public List<Product> getAllProducts() {
        Session session = sessionFactory.openSession();
        List<Product> products = session.createQuery("from Product", Product.class).list();
        session.close();
        return products;
    }

    public void updateProduct(Product product) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(product);
        transaction.commit();
        session.close();
    }

    public void deleteProduct(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Product product = session.get(Product.class, id);
        if (product != null) {
            session.delete(product);
        }
        transaction.commit();
        session.close();
    }

    public List<Product> findByUserId(Long userId) {
        Session session = sessionFactory.openSession();
        List<Product> products = session.createQuery("from Product p where p.user.id = :userId", Product.class)
                .setParameter("userId", userId)
                .list();
        session.close();
        return products;
    }


}
