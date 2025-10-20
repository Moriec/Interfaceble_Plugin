package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 3 методами для класса UserService
 */
public interface UserServiceDeleteUserFindUsersByNameGetUserCountInterface {

    public boolean deleteUser(Long id);

    public List<User> findUsersByName(String name);

    public int getUserCount();
}
