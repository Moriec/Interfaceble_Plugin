package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 4 методами для класса UserService
 */
public interface UserServiceGetUserByIdUserExistsByEmailGetUserCountClearAllUsersInterface {

    public Optional<User> getUserById(Long id);

    public boolean userExistsByEmail(String email);

    public int getUserCount();

    public void clearAllUsers();
}
