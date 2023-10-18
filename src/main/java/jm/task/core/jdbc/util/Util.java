package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util { // реализуйте настройку соеденения с БД
    public static Connection getConnectionMYSQL() throws SQLException {
        String USERNAME = "root";
        String PASSWORD = "IsmaMbenz_009";
        String URL = "jdbc:mysql://localhost:3306/userki";

        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}




