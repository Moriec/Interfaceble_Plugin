package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 3 методами для класса UserService
 */
public interface UserServiceGetUserByIdCreateUserGetUserCountInterface {

    public Optional<User> getUserById(Long id);

    public User createUser(String name, String email);

    public int getUserCount();
}
