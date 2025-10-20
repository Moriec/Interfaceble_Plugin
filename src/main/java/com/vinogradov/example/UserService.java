package com.vinogradov.example;

import java.util.List;
import java.util.Optional;

/**
 * Пример сервиса для работы с пользователями
 */
public class UserService {
    
    private List<User> users;

    public UserService() {
        this.users = new java.util.ArrayList<>();
    }
    

    public Optional<User> getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public User createUser(String name, String email) {
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(name);
        user.setEmail(email);
        users.add(user);
        return user;
    }
    

    public User updateUser(User user) {
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        return user;
    }

    public boolean deleteUser(Long id) {
        return users.removeIf(user -> user.getId().equals(id));
    }
    

    public List<User> getAllUsers() {
        return new java.util.ArrayList<>(users);
    }
    

    public List<User> findUsersByName(String name) {
        return users.stream()
                .filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public boolean userExistsByEmail(String email) {
        return users.stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public int getUserCount() {
        return users.size();
    }
    

    public void clearAllUsers() {
        users.clear();
    }
    
    // Приватный метод - не должен попасть в интерфейс
    private void logUserAction(String action, User user) {
        System.out.println("User action: " + action + " for user: " + user.getName());
    }
    
    // Статический метод - не должен попасть в интерфейс
    public static String getServiceVersion() {
        return "1.0.0";
    }
}
