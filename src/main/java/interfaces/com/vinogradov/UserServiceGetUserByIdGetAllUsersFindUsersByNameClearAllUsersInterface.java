package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 4 методами для класса UserService
 */
public interface UserServiceGetUserByIdGetAllUsersFindUsersByNameClearAllUsersInterface {

    public Optional<User> getUserById(Long id);

    public List<User> getAllUsers();

    public List<User> findUsersByName(String name);

    public void clearAllUsers();
}
