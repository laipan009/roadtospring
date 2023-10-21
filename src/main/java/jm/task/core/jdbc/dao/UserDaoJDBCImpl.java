package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connectionMYSQL = Util.getInstance().getConnectionMYSQL();
    private Statement statement;
    private PreparedStatement preparedStatement;
    private final String CREATE_TABLE = """
            CREATE TABLE userok (
                `id` INT NOT NULL AUTO_INCREMENT,
                `name` VARCHAR(45) NOT NULL,
                `surname` VARCHAR(45) NOT NULL,
                `age` INT NOT NULL,
                PRIMARY KEY (`id`));
            """;
    private final String DROP_TABLE = """
            DROP TABLE `userki`.`userok`;
            """;
    private final String INSERT_INTO_TABLE_userok = """
            INSERT INTO `userki`.`userok`
            ( `name`, `surname`, `age`)
            VALUES
            (?, ?, ?);
            """;
    private final String DELETE_FROM_TABLE_userok = """
            DELETE FROM `userki`.`userok`
            WHERE id = ?
            """;
    private final String SELECT_ALL_FROM_userok = """
            SELECT * FROM `userki`.`userok`                        
            """;
    private final String DELETE_ALL_FROM_userok = """
            DELETE FROM `userki`.`userok`
            WHERE id > 0
            """;

    public UserDaoJDBCImpl() throws SQLException {
    }

    public void createUsersTable() throws SQLException {
        try {
            statement = connectionMYSQL.createStatement();
            connectionMYSQL.setAutoCommit(false);
            statement.executeUpdate(CREATE_TABLE);
            connectionMYSQL.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            connectionMYSQL.rollback();
        } finally {
            if (!statement.isClosed()) {
                statement.close();
            }
            connectionMYSQL.setAutoCommit(true);
        }
    }

    public void dropUsersTable() throws SQLException {
        try {
            statement = connectionMYSQL.createStatement();
            connectionMYSQL.setAutoCommit(false);
            var execute = statement.executeUpdate(DROP_TABLE);
            if (execute == 0) {
                System.out.println("DB deleted");
                connectionMYSQL.commit();
            } else {
                System.out.println("DB not deleted");
                connectionMYSQL.rollback();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            connectionMYSQL.rollback();
        } finally {
            if (!statement.isClosed()) {
                statement.close();
            }
            connectionMYSQL.setAutoCommit(true);
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        try {
            preparedStatement = connectionMYSQL.prepareStatement(INSERT_INTO_TABLE_userok);
            connectionMYSQL.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate > 0) {
                connectionMYSQL.commit();
                System.out.println("User created by id ");
            }
        } catch (SQLException e) {
            connectionMYSQL.rollback();
            System.out.println(e.getMessage());
        } finally {
            if (!preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            connectionMYSQL.setAutoCommit(true);
        }
    }

    public void removeUserById(long id) throws SQLException {
        try {
            preparedStatement = connectionMYSQL.prepareStatement(DELETE_FROM_TABLE_userok);
            connectionMYSQL.setAutoCommit(false);
            preparedStatement.setInt(1, (int) id);
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate > 0) {
                connectionMYSQL.commit();
                System.out.println("User deleted by id " + id);
            }
        } catch (SQLException e) {
            connectionMYSQL.rollback();
            System.out.println(e.getMessage());
        } finally {
            if (!preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            connectionMYSQL.setAutoCommit(true);
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> userki = new ArrayList<>();
        try {
            preparedStatement = connectionMYSQL.prepareStatement(SELECT_ALL_FROM_userok);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userki.add(getUser(resultSet));
            }
            return userki;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (!preparedStatement.isClosed()) {
                preparedStatement.close();
            }
        }
        return userki;
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getByte("age"));
        user.setId((long) resultSet.getInt("id"));
        return user;
    }

    public void cleanUsersTable() throws SQLException {
        try {
            preparedStatement = connectionMYSQL.prepareStatement(DELETE_ALL_FROM_userok);
            connectionMYSQL.setAutoCommit(false);
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate > 0) {
                connectionMYSQL.commit();
                System.out.println("Users deleted");
            }
        } catch (SQLException e) {
            connectionMYSQL.rollback();
            System.out.println(e.getMessage());
        } finally {
            if (!preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            connectionMYSQL.setAutoCommit(true);
        }
    }
}
