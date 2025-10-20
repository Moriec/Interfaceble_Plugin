# Interface Generator Maven Plugin

Maven плагин для автоматической генерации интерфейсов на основе публичных методов классов.

## Описание

Этот плагин анализирует Java классы и автоматически создает интерфейсы, содержащие все публичные нестатичные методы исходного класса. Плагин использует JavaParser для анализа исходного кода и генерации соответствующих интерфейсов.

## Возможности

- Автоматический анализ Java классов
- Генерация интерфейсов на основе публичных методов
- Настраиваемые параметры (пакет, суффикс, директории)
- Игнорирование статических и приватных методов
- Сохранение аннотаций методов
- Поддержка JavaDoc комментариев
- Генерация интерфейсов прямо в исходной директории
- Автоматическое добавление необходимых импортов
- Исключение плагина из генерации интерфейсов

## Установка

1. Соберите плагин:
```bash
mvn clean install
```

2. Добавьте плагин в ваш проект:

```xml
<plugin>
    <groupId>com.vinogradov</groupId>
    <artifactId>interface-generator-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <phase>process-classes</phase>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <sourceDirectory>${project.basedir}/src/main/java</sourceDirectory>
                <outputDirectory>${project.basedir}/src/main/java</outputDirectory>
                <interfacePackage>com.vinogradov</interfacePackage>
                <interfaceSuffix>Interface</interfaceSuffix>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Параметры конфигурации

| Параметр | Описание | Значение по умолчанию |
|----------|----------|----------------------|
| `sourceDirectory` | Директория с исходными Java файлами | `${project.basedir}/src/main/java` |
| `outputDirectory` | Директория для генерации интерфейсов | `${project.basedir}/src/main/java` |
| `interfacePackage` | Пакет для генерируемых интерфейсов | `com.vinogradov` |
| `interfaceSuffix` | Суффикс для имен интерфейсов | `Interface` |

## Пример использования

### Исходный класс

```java
public class UserService {
    
    public Optional<User> getUserById(Long id) {
        // реализация
    }
    
    public User createUser(String name, String email) {
        // реализация
    }
    
    public boolean deleteUser(Long id) {
        // реализация
    }
    
    private void logUserAction(String action, User user) {
        // приватный метод - не попадет в интерфейс
    }
    
    public static String getServiceVersion() {
        // статический метод - не попадет в интерфейс
    }
}
```

### Сгенерированный интерфейс

```java
package com.vinogradov;

import java.util.List;
import java.util.Optional;
import com.vinogradov.example.User;

/**
 * Автоматически сгенерированный интерфейс для класса UserService
 */
public interface UserServiceInterface {
    
    public Optional<User> getUserById(Long id);
    
    public User createUser(String name, String email);
    
    public User updateUser(User user);
    
    public boolean deleteUser(Long id);
    
    public List<User> getAllUsers();
    
    public List<User> findUsersByName(String name);
    
    public boolean userExistsByEmail(String email);
    
    public int getUserCount();
    
    public void clearAllUsers();
}
```

## Запуск

Плагин автоматически выполняется в фазе `process-classes`. Для запуска:

```bash
mvn clean compile
```

Или для принудительной генерации интерфейсов:

```bash
mvn process-classes
```

## Тестирование

Для тестирования плагина в основном проекте:

1. Выполните команду:
```bash
mvn clean compile
```

2. Проверьте сгенерированные интерфейсы в `src/main/java/com/vinogradov/`

## Структура проекта

```
├── src/main/java/com/vinogradov/
│   ├── InterfaceGeneratorMojo.java    # Основной класс плагина
│   ├── UserInterface.java             # Сгенерированный интерфейс
│   ├── UserServiceInterface.java      # Сгенерированный интерфейс
│   └── example/
│       ├── UserService.java           # Пример класса
│       └── User.java                  # Модель данных
└── pom.xml                           # Конфигурация плагина
```

## Требования

- Java 11+
- Maven 3.6+
- JavaParser 3.24.4+

## Лицензия

MIT License
