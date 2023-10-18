package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static UserDaoJDBCImpl userDaoJDBC;

    private UserDaoJDBCImpl() {

    }

    public static UserDaoJDBCImpl initDao() {
        return userDaoJDBC = new UserDaoJDBCImpl();
    }

    public void createUsersTable() {
        String sql = """
                CREATE TABLE userok (
                    `id` INT NOT NULL AUTO_INCREMENT,
                    `name` VARCHAR(45) NOT NULL,
                    `surname` VARCHAR(45) NOT NULL,
                    `age` INT NOT NULL,
                    PRIMARY KEY (`id`));
                """;
        try (Connection connectionMYSQL = Util.getConnectionMYSQL();
             Statement statement = connectionMYSQL.createStatement()) {
            boolean execute = statement.execute(sql);
            if (!execute) {
                System.out.println("DB created");
            } else {
                System.out.println("DB not created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropUsersTable() {
        String sql = """
                DROP TABLE `userki`.`userok`;
                """;
        try (Connection connectionMYSQL = Util.getConnectionMYSQL();
             Statement statement = connectionMYSQL.createStatement()) {
            var execute = statement.executeUpdate(sql);
            if (execute == 0) {
                System.out.println("DB deleted");
            } else {
                System.out.println("DB not deleted");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = """
                INSERT INTO `userki`.`userok`
                ( `name`, `surname`, `age`)
                VALUES
                (?, ?, ?);
                """;
        try (Connection connectionMYSQL = Util.getConnectionMYSQL();
             PreparedStatement preparedStatement = connectionMYSQL.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate > 0) {
                System.out.println("User created by id ");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeUserById(long id) {
        String sql = """
                DELETE FROM `userki`.`userok`
                WHERE id = ?
                """;
        try (Connection connectionMYSQL = Util.getConnectionMYSQL();
             PreparedStatement preparedStatement = connectionMYSQL.prepareStatement(sql)) {
            preparedStatement.setInt(1, (int) id);
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate > 0) {
                System.out.println("User deleted by id " + id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<User> getAllUsers() {
        String sql = """
                SELECT * FROM `userki`.`userok`                        
                """;
        List<User> userki = new ArrayList<>();
        try (Connection connectionMYSQL = Util.getConnectionMYSQL();
             PreparedStatement preparedStatement = connectionMYSQL.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userki.add(getUser(resultSet));
            }
            return userki;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

    public void cleanUsersTable() {
        String sql = """
                DELETE FROM `userki`.`userok`
                WHERE id > 0
                """;

        try (Connection connectionMYSQL = Util.getConnectionMYSQL();
             PreparedStatement preparedStatement = connectionMYSQL.prepareStatement(sql)) {
            int executeUpdate = preparedStatement.executeUpdate();
            if (executeUpdate > 0) {
                System.out.println("Users deleted");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
