package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT , " +
                    "name VARCHAR(100) NULL,\n" +
                    "last_name VARCHAR(100) NULL,\n" +
                    "age TINYINT NULL,\n" +
                    "PRIMARY KEY (id));");
        } catch (SQLException e) {
            System.out.println("Fail to create table: " + e);
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            System.out.println("Fail to drop table: " + e);
        }
    }

    public void saveUser(User user) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(name, last_name, age)" +
                     "VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setByte(3, user.getAge());
            preparedStatement.execute();
            try {
                connection.commit();
                System.out.printf("User с именем – \"%s %s\" добавлен в базу данных%n", user.getName(), user.getLastName());
            } catch (SQLException e) {
                System.out.println("Ошибка при коммите нового пользователя " + e);
                try {
                    connection.rollback();
                } catch (SQLException exception) {
                    System.out.println("Ошибка при попытке роллбэка добавления нового пользователя " + exception);;
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при попытке добавления нового пользователя: " + e);
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            try {
                connection.commit();
            } catch (SQLException e) {
                System.out.println("Ошибка при коммите удаления пользователя " + e);
                try {
                    connection.rollback();
                } catch (SQLException exception) {
                    System.out.println("Ошибка при роллбэк удаления пользователя");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении пользователя: " + e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка пользователей: " + e);
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE users");
        } catch (SQLException e) {
            System.out.println("Ошибка при очистке таблицы \"Users\": " + e);
        }
    }
}
