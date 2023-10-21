package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util { // реализуйте настройку соеденения с БД
    private static String USERNAME = "root";
    private static String PASSWORD = "IsmaMbenz_009";
    private static String URL = "jdbc:mysql://localhost:3306/userki";
    private final Connection connection;
    private static Util INSTANCE;

    private Util() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Util getInstance() throws SQLException {
        if (INSTANCE == null || INSTANCE.getConnectionMYSQL().isClosed()) {
            INSTANCE = new Util();
        }
        return INSTANCE;
    }

    public Connection getConnectionMYSQL() {
        return connection;
    }


}




