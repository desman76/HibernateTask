package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Util {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String URL = "jdbc:mysql://localhost:3306/jmpp";
    private static final String DIALECT = "org.hibernate.dialect.MySQLDialect";
    private static final String HBM2DDL = "none";
    private static final String SHOW_SQL = "true";
    private static final String FORMAT_SQL = "true";
    private static final String AUTOCOMMIT = "false";
    private static SessionFactory sessionFactory;

    private Util() {
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Ошибка получения соединения: " + e);
        }
        return connection;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Properties properties = new Properties();
            properties.setProperty(Environment.URL, URL);
            properties.setProperty(Environment.USER, USERNAME);
            properties.setProperty(Environment.PASS, PASSWORD);
            properties.setProperty(Environment.DRIVER, DRIVER);
            properties.setProperty(Environment.DIALECT, DIALECT);
            properties.setProperty(Environment.HBM2DDL_AUTO, HBM2DDL);
            properties.setProperty(Environment.SHOW_SQL, SHOW_SQL);
            properties.setProperty(Environment.FORMAT_SQL, FORMAT_SQL);
            properties.setProperty(Environment.AUTOCOMMIT, AUTOCOMMIT);

            try {
                sessionFactory = new Configuration()
                        .addProperties(properties)
                        .addAnnotatedClass(User.class)
                        .buildSessionFactory();

            } catch (HibernateException e) {
                System.out.println("Ошибка получения соединения: " + e);
            }
        }
        return sessionFactory;
    }
}
