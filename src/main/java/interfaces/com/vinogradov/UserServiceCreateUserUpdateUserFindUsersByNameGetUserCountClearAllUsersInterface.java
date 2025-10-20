package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 5 методами для класса UserService
 */
public interface UserServiceCreateUserUpdateUserFindUsersByNameGetUserCountClearAllUsersInterface {

    public User createUser(String name, String email);

    public User updateUser(User user);

    public List<User> findUsersByName(String name);

    public int getUserCount();

    public void clearAllUsers();
}
