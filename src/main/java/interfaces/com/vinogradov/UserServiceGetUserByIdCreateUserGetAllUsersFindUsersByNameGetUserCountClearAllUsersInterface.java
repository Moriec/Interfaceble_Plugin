package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 6 методами для класса UserService
 */
public interface UserServiceGetUserByIdCreateUserGetAllUsersFindUsersByNameGetUserCountClearAllUsersInterface {

    public Optional<User> getUserById(Long id);

    public User createUser(String name, String email);

    public List<User> getAllUsers();

    public List<User> findUsersByName(String name);

    public int getUserCount();

    public void clearAllUsers();
}
