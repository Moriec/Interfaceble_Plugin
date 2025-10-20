package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 3 методами для класса UserService
 */
public interface UserServiceCreateUserUpdateUserGetUserCountInterface {

    public User createUser(String name, String email);

    public User updateUser(User user);

    public int getUserCount();
}
