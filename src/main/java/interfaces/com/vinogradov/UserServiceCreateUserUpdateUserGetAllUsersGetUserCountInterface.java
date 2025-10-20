package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 4 методами для класса UserService
 */
public interface UserServiceCreateUserUpdateUserGetAllUsersGetUserCountInterface {

    public User createUser(String name, String email);

    public User updateUser(User user);

    public List<User> getAllUsers();

    public int getUserCount();
}
