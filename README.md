# Interfaceble Plugin

Вы когда-нибудь чувствовали, что вашему коду не хватает интерфейсов?
Вам казалось, что принципы SOLID недостаточно усложняют вашу жизнь?

Представляем Interfaceble Plugin - ультимативное решение для тех, кто считает, что каждая возможная комбинация методов заслуживает отдельного интерфейса!

## О чем это?

Этот Maven-плагин берет ваши классы и делает с ними то, о чем вы боялись спросить. Он анализирует все публичные методы класса и генерирует интерфейсы для абсолютно всех их комбинаций.

Да, вы не ослышались. Если у вас есть класс с 5 методами, мы сгенерируем интерфейсы для:
- Каждой пары методов
- Каждой тройки методов
- Каждой четвёрки методов
- ...и всех остальных подмножеств!

Потому что кто знает, когда вам срочно понадобится UserServiceGetUserByIdAndCreateUserInterface, но строго без метода deleteUser? Теперь он у вас будет!

## Ключевые особенности

- Комбинаторный взрыв: Генерация огромного количества интерфейсов для одного класса. Ваш проект станет тяжелее, солиднее и внушительнее!
- Безумный нейминг: Названия интерфейсов формируются склеиванием имен всех входящих в них методов. Наслаждайтесь шедеврами вроде OrderServiceProcessPaymentValidateUserCheckStockSendEmailInterface.
- Полная автоматизация: Работает на фазе process-classes. Вы просто пишите код, а мы заботливо захламляем структуру вашего проекта.

## Установка

Соберите этот шедевр локально:

```bash
mvn clean install
```

## Использование

Добавьте это в ваш pom.xml (на свой страх и риск):

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
                <!-- Откуда брать классы -->
                <sourceDirectory>${project.basedir}/src/main/java</sourceDirectory>
                <!-- Куда складывать сгенерированные интерфейсы -->
                <outputDirectory>${project.basedir}/src/main/java/interfaces</outputDirectory>
                <interfacePackage>com.vinogradov.generated</interfacePackage>
                <generateCombinations>true</generateCombinations>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Предупреждение

Использование этого плагина на классах с более чем 10-15 методами может привести к:
1. Острой боли в глазах при просмотре структуры проекта.
2. Истерическому смеху коллег при Code Review.
3. Исчерпанию inodes на файловой системе.
