package jm.task.core.jdbc.dao;

import java.util.List;

public interface BasicDao<T> {
    void createUsersTable();

    void dropUsersTable();

    void saveUser(T t);

    void removeUserById(long id);

    List<T> getAllUsers();

    void cleanUsersTable();
}
