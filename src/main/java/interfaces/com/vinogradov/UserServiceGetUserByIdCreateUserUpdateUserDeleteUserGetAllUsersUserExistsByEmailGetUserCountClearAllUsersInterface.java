package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 8 методами для класса UserService
 */
public interface UserServiceGetUserByIdCreateUserUpdateUserDeleteUserGetAllUsersUserExistsByEmailGetUserCountClearAllUsersInterface {

    public Optional<User> getUserById(Long id);

    public User createUser(String name, String email);

    public User updateUser(User user);

    public boolean deleteUser(Long id);

    public List<User> getAllUsers();

    public boolean userExistsByEmail(String email);

    public int getUserCount();

    public void clearAllUsers();
}
