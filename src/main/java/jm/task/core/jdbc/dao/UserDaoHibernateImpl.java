package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                session.createNativeQuery("CREATE TABLE IF NOT EXISTS users (" +
                        "id BIGINT NOT NULL AUTO_INCREMENT , " +
                        "name VARCHAR(100) NULL,\n" +
                        "last_name VARCHAR(100) NULL,\n" +
                        "age TINYINT NULL,\n" +
                        "PRIMARY KEY (id));")
                        .executeUpdate();
                session.getTransaction().commit();
                System.out.println("table created");
            } catch (Exception e) {
                System.out.println("Ошибка при создании таблицы: " + e);
                session.getTransaction().rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
                System.out.println("table removed");
            } catch (Exception e) {
                System.out.println("Ошибка при удалении таблицы: " + e);
                session.getTransaction().rollback();
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(User user) {
        try (Session session = Util.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
                System.out.printf("User с именем – \"%s %s\" добавлен в базу данных%n",
                        user.getName(), user.getLastName());
            } catch (Exception e) {
                System.out.println("Ошибка добавления нового пользователя " + e);
                session.getTransaction().rollback();
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                User user = session.get(User.class, id);
                if (user != null) {
                    session.delete(user);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Ошибка при удалении пользователя: " + e);
                session.getTransaction().rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();
            users = session.createQuery("FROM User").list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                session.createQuery("DELETE FROM User")
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Ошибка при очистке таблицы: " + e);
                session.getTransaction().rollback();
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
