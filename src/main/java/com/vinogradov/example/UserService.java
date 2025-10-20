package com.vinogradov.example;

import java.util.List;
import java.util.Optional;

/**
 * Пример сервиса для работы с пользователями
 */
public class UserService {
    
    private List<User> users;
    
    /**
     * Конструктор по умолчанию
     */
    public UserService() {
        this.users = new java.util.ArrayList<>();
    }
    
    /**
     * Получить пользователя по ID
     * @param id идентификатор пользователя
     * @return пользователь или пустой Optional
     */
    public Optional<User> getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
    
    /**
     * Создать нового пользователя
     * @param name имя пользователя
     * @param email email пользователя
     * @return созданный пользователь
     */
    public User createUser(String name, String email) {
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName(name);
        user.setEmail(email);
        users.add(user);
        return user;
    }
    
    /**
     * Обновить информацию о пользователе
     * @param user пользователь для обновления
     * @return обновленный пользователь
     */
    public User updateUser(User user) {
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        return user;
    }
    
    /**
     * Удалить пользователя
     * @param id идентификатор пользователя
     * @return true если пользователь был удален
     */
    public boolean deleteUser(Long id) {
        return users.removeIf(user -> user.getId().equals(id));
    }
    
    /**
     * Получить всех пользователей
     * @return список всех пользователей
     */
    public List<User> getAllUsers() {
        return new java.util.ArrayList<>(users);
    }
    
    /**
     * Найти пользователей по имени
     * @param name имя для поиска
     * @return список найденных пользователей
     */
    public List<User> findUsersByName(String name) {
        return users.stream()
                .filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }
    
    /**
     * Проверить существует ли пользователь с указанным email
     * @param email email для проверки
     * @return true если пользователь существует
     */
    public boolean userExistsByEmail(String email) {
        return users.stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
    
    /**
     * Получить количество пользователей
     * @return количество пользователей
     */
    public int getUserCount() {
        return users.size();
    }
    
    /**
     * Очистить всех пользователей
     */
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
