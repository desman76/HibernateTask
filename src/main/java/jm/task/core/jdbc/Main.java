package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Bob", "Huston", (byte) 19);
        userService.saveUser("Smith", "Prank", (byte)60);
        userService.saveUser("Jack", "Sparrow", (byte)250);
        userService.saveUser("Andrew", "Collins", (byte)35);

        userService.removeUserById(9);

        System.out.println(userService.getAllUsers());

        userService.cleanUsersTable();

        userService.dropUsersTable();

        System.exit(0);
    }
}
