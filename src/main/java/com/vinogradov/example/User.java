package com.vinogradov.example;

/**
 * Модель пользователя
 */
public class User implements com.vinogradov.UserInterface {
    private Long id;
    private String name;
    private String email;
    
    /**
     * Конструктор по умолчанию
     */
    public User() {
    }
    
    /**
     * Конструктор с параметрами
     * @param id идентификатор
     * @param name имя
     * @param email email
     */
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    /**
     * Получить идентификатор пользователя
     * @return идентификатор
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Установить идентификатор пользователя
     * @param id идентификатор
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Получить имя пользователя
     * @return имя
     */
    public String getName() {
        return name;
    }
    
    /**
     * Установить имя пользователя
     * @param name имя
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Получить email пользователя
     * @return email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Установить email пользователя
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null ? id.equals(user.id) : user.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
