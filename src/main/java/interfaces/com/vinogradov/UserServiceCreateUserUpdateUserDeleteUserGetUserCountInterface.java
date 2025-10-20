package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 4 методами для класса UserService
 */
public interface UserServiceCreateUserUpdateUserDeleteUserGetUserCountInterface {

    public User createUser(String name, String email);

    public User updateUser(User user);

    public boolean deleteUser(Long id);

    public int getUserCount();
}
