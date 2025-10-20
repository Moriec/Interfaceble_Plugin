package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 5 методами для класса UserService
 */
public interface UserServiceGetUserByIdGetAllUsersUserExistsByEmailGetUserCountClearAllUsersInterface {

    public Optional<User> getUserById(Long id);

    public List<User> getAllUsers();

    public boolean userExistsByEmail(String email);

    public int getUserCount();

    public void clearAllUsers();
}
