package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.ConfigHibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionFactory = ConfigHibernate.getSessionFactory();
    private Session session;

    @Override
    public void createUsersTable() {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

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
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String sql = """
                    DROP TABLE IF EXISTS userki.userok;
                    """;

            Query<User> query = session.createSQLQuery(sql);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

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
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            Query query = session.createQuery("DELETE from User WHERE id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userki = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            userki = session.createQuery("from User", User.class).list();
            session.getTransaction().commit();
            return userki;
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        System.out.println("Query suck dick, users not found");
        return userki;
    }

    @Override
    public void cleanUsersTable() {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("DELETE from User WHERE id > 0");
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }
}
