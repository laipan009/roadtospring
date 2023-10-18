package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final SessionFactory sessionFactory = new Configuration()
            .addAnnotatedClass(User.class)
            .buildSessionFactory();
    private static UserDaoHibernateImpl userDaoHibernate;

    private UserDaoHibernateImpl() {

    }

    public static UserDaoHibernateImpl initHiberImpl() {
        return userDaoHibernate = new UserDaoHibernateImpl();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = """
                    CREATE TABLE IF NOT EXISTS userok (
                        `id` INT NOT NULL AUTO_INCREMENT,
                        `name` VARCHAR(45) NOT NULL,
                        `surname` VARCHAR(45) NOT NULL,
                        `age` INT NOT NULL,
                        PRIMARY KEY (`id`));
                    """;

            Query<User> query = session.createSQLQuery(sql);
            query.executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = """
                    DROP TABLE IF EXISTS userki.userok;
                    """;

            Query<User> query = session.createSQLQuery(sql);
            query.executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = """
                    INSERT INTO `userki`.`userok`
                    ( `name`, `surname`, `age`)
                    VALUES
                    (?, ?, ?);
                    """;

            Query query = session.createSQLQuery(sql);
            query.setParameter(1, name);
            query.setParameter(2, lastName);
            query.setParameter(3, age);
            query.executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Query query = session.createQuery("DELETE from User WHERE id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("DELETE from User WHERE id > 0");
            query.executeUpdate();
            transaction.commit();
        }
    }
}
