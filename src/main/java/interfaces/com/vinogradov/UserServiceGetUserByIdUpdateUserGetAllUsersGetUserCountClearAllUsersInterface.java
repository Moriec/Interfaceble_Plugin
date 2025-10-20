package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 5 методами для класса UserService
 */
public interface UserServiceGetUserByIdUpdateUserGetAllUsersGetUserCountClearAllUsersInterface {

    public Optional<User> getUserById(Long id);

    public User updateUser(User user);

    public List<User> getAllUsers();

    public int getUserCount();

    public void clearAllUsers();
}
