package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс с 2 методами для класса UserService
 */
public interface UserServiceGetUserByIdFindUsersByNameInterface {

    public Optional<User> getUserById(Long id);

    public List<User> findUsersByName(String name);
}
