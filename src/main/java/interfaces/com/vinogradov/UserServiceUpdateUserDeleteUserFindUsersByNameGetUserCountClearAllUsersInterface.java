package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 5 методами для класса UserService
 */
public interface UserServiceUpdateUserDeleteUserFindUsersByNameGetUserCountClearAllUsersInterface {

    public User updateUser(User user);

    public boolean deleteUser(Long id);

    public List<User> findUsersByName(String name);

    public int getUserCount();

    public void clearAllUsers();
}
