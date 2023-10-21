package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class ConfigHibernate {
    private static Properties properties;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        try {
            properties = new Properties();

            properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            properties.put(Environment.URL, "jdbc:mysql://localhost:3306/userki");
            properties.put(Environment.USER, "root");
            properties.put(Environment.PASS, "IsmaMbenz_009");
            properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
            properties.put(Environment.SHOW_SQL, "true");
            properties.put(Environment.HBM2DDL_AUTO, "update");

            sessionFactory = new Configuration()
                    .addAnnotatedClass(User.class)
                    .setProperties(properties)
                    .buildSessionFactory();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return sessionFactory;
    }







}