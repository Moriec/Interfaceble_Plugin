package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 6 методами для класса UserService
 */
public interface UserServiceGetUserByIdUpdateUserDeleteUserGetAllUsersUserExistsByEmailClearAllUsersInterface {

    public Optional<User> getUserById(Long id);

    public User updateUser(User user);

    public boolean deleteUser(Long id);

    public List<User> getAllUsers();

    public boolean userExistsByEmail(String email);

    public void clearAllUsers();
}
