package com.vinogradov;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.printer.DefaultPrettyPrinter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Maven плагин для автоматической генерации интерфейсов на основе публичных методов классов
 */
@Mojo(name = "generate", defaultPhase = org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES)
public class InterfaceGeneratorMojo extends AbstractMojo {

    /**
     * Maven проект
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * Директория с исходными файлами для анализа
     */
    @Parameter(defaultValue = "${project.basedir}/src/main/java")
    private String sourceDirectory;

    /**
     * Директория для генерации интерфейсов
     */
    @Parameter(defaultValue = "${project.basedir}/src/main/java/interfaces")
    private String outputDirectory;

    /**
     * Пакет для генерируемых интерфейсов
     */
    @Parameter(defaultValue = "generated.interfaces")
    private String interfacePackage;

    /**
     * Суффикс для имен интерфейсов
     */
    @Parameter(defaultValue = "Interface")
    private String interfaceSuffix;

    /**
     * Генерировать ли интерфейсы со всеми возможными комбинациями методов
     */
    @Parameter(defaultValue = "true")
    private boolean generateCombinations;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Начинаем генерацию интерфейсов...");
        getLog().info("Исходная директория: " + sourceDirectory);
        getLog().info("Выходная директория: " + outputDirectory);
        getLog().info("Пакет интерфейсов: " + interfacePackage);

        try {
            // Создаем выходную директорию
            Path outputPath = Paths.get(outputDirectory);
            Files.createDirectories(outputPath);

            // Сканируем исходные файлы
            scanSourceFiles(Paths.get(sourceDirectory), outputPath);

            getLog().info("Генерация интерфейсов завершена успешно!");
        } catch (IOException e) {
            throw new MojoExecutionException("Ошибка при генерации интерфейсов", e);
        }
    }

    /**
     * Рекурсивно сканирует директорию с исходными файлами
     */
    private void scanSourceFiles(Path sourcePath, Path outputPath) throws IOException {
        if (!Files.exists(sourcePath)) {
            getLog().warn("Исходная директория не существует: " + sourcePath);
            return;
        }

        Files.walk(sourcePath)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(javaFile -> {
                    try {
                        processJavaFile(javaFile, outputPath);
                    } catch (IOException e) {
                        getLog().error("Ошибка при обработке файла: " + javaFile, e);
                    }
                });
    }

    /**
     * Обрабатывает отдельный Java файл
     */
    private void processJavaFile(Path javaFile, Path outputPath) throws IOException {
        getLog().info("Обрабатываем файл: " + javaFile);

        String content = Files.readString(javaFile);
        JavaParser parser = new JavaParser();
        Optional<CompilationUnit> cu = parser.parse(content).getResult();

        if (cu.isPresent()) {
        cu.get().findAll(ClassOrInterfaceDeclaration.class)
                .stream()
                .filter(decl -> !decl.isInterface()) // Только классы, не интерфейсы
                .filter(decl -> !decl.getNameAsString().contains("Interface")) // Исключаем уже сгенерированные интерфейсы
                .filter(decl -> !decl.getNameAsString().equals("InterfaceGeneratorMojo")) // Исключаем сам плагин
                .forEach(classDecl -> {
                        try {
                            generateInterfaceForClass(classDecl, javaFile, outputPath);
                        } catch (IOException e) {
                            getLog().error("Ошибка при генерации интерфейса для класса: " + classDecl.getName(), e);
                        }
                    });
        }
    }

    /**
     * Генерирует интерфейс для указанного класса
     */
    private void generateInterfaceForClass(ClassOrInterfaceDeclaration classDecl, Path originalFile, Path outputPath) throws IOException {
        String className = classDecl.getNameAsString();
        
        // Находим все публичные методы в классе
        List<MethodDeclaration> publicMethods = classDecl.findAll(MethodDeclaration.class)
                .stream()
                .filter(method -> method.isPublic() && !method.isStatic())
                .toList();

        if (publicMethods.isEmpty()) {
            getLog().info("В классе " + className + " не найдено публичных методов для генерации интерфейса");
            return;
        }

        // Генерируем основной интерфейс со всеми методами
        generateMainInterface(className, publicMethods, outputPath);

        // Генерируем интерфейсы с комбинациями методов
        if (generateCombinations && publicMethods.size() > 1) {
            generateCombinationInterfaces(className, publicMethods, outputPath);
        }
    }

    /**
     * Генерирует основной интерфейс со всеми методами
     */
    private void generateMainInterface(String className, List<MethodDeclaration> publicMethods, Path outputPath) throws IOException {
        String interfaceName = className + interfaceSuffix;
        getLog().info("Генерируем основной интерфейс " + interfaceName + " для класса " + className);

        CompilationUnit interfaceCu = createInterfaceCompilationUnit(interfaceName, className, publicMethods);
        saveInterface(interfaceCu, interfaceName, outputPath);
    }

    /**
     * Генерирует интерфейсы со всеми возможными комбинациями методов
     */
    private void generateCombinationInterfaces(String className, List<MethodDeclaration> publicMethods, Path outputPath) throws IOException {
        getLog().info("Генерируем интерфейсы с комбинациями методов для класса " + className);
        
        // Генерируем все возможные комбинации методов (от 2 до n-1 методов)
        for (int size = 2; size < publicMethods.size(); size++) {
            List<List<MethodDeclaration>> combinations = generateCombinations(publicMethods, size);
            
            for (List<MethodDeclaration> combination : combinations) {
                String combinationName = generateCombinationName(className, combination);
                getLog().info("Генерируем интерфейс " + combinationName + " с " + combination.size() + " методами");
                
                CompilationUnit interfaceCu = createInterfaceCompilationUnit(combinationName, className, combination);
                saveInterface(interfaceCu, combinationName, outputPath);
            }
        }
    }

    /**
     * Генерирует все возможные комбинации заданного размера
     */
    private List<List<MethodDeclaration>> generateCombinations(List<MethodDeclaration> methods, int size) {
        List<List<MethodDeclaration>> result = new ArrayList<>();
        generateCombinationsHelper(methods, size, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * Вспомогательный метод для генерации комбинаций
     */
    private void generateCombinationsHelper(List<MethodDeclaration> methods, int size, int start, 
                                          List<MethodDeclaration> current, List<List<MethodDeclaration>> result) {
        if (current.size() == size) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < methods.size(); i++) {
            current.add(methods.get(i));
            generateCombinationsHelper(methods, size, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    /**
     * Генерирует имя интерфейса на основе комбинации методов
     */
    private String generateCombinationName(String className, List<MethodDeclaration> methods) {
        StringBuilder nameBuilder = new StringBuilder(className);
        
        for (MethodDeclaration method : methods) {
            String methodName = method.getNameAsString();
            // Преобразуем имя метода в CamelCase для имени интерфейса
            String capitalizedMethodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
            nameBuilder.append(capitalizedMethodName);
        }
        
        nameBuilder.append(interfaceSuffix);
        return nameBuilder.toString();
    }

    /**
     * Создает CompilationUnit для интерфейса
     */
    private CompilationUnit createInterfaceCompilationUnit(String interfaceName, String className, List<MethodDeclaration> methods) {
        CompilationUnit interfaceCu = new CompilationUnit();
        
        // Добавляем пакет
        if (!interfacePackage.isEmpty()) {
            interfaceCu.setPackageDeclaration(interfacePackage);
        }
        
        // Добавляем необходимые импорты
        interfaceCu.addImport("java.util.List");
        interfaceCu.addImport("java.util.Optional");
        interfaceCu.addImport("com.vinogradov.example.User");

        // Создаем объявление интерфейса
        ClassOrInterfaceDeclaration interfaceDecl = interfaceCu.addInterface(interfaceName);

        // Добавляем комментарий
        String comment = methods.size() == 1 ? 
            "Автоматически сгенерированный интерфейс с методом " + methods.get(0).getNameAsString() + " для класса " + className :
            "Автоматически сгенерированный интерфейс с " + methods.size() + " методами для класса " + className;
        interfaceDecl.setJavadocComment(comment);

        // Добавляем методы в интерфейс
        for (MethodDeclaration method : methods) {
            MethodDeclaration interfaceMethod = new MethodDeclaration();
            interfaceMethod.setName(method.getName());
            interfaceMethod.setType(method.getType());
            interfaceMethod.setPublic(true);
            
            // Убираем тело метода для интерфейса
            interfaceMethod.removeBody();

            // Копируем параметры
            for (com.github.javaparser.ast.body.Parameter param : method.getParameters()) {
                interfaceMethod.addParameter(param.getType(), param.getNameAsString());
            }

            // Копируем аннотации
            method.getAnnotations().forEach(interfaceMethod::addAnnotation);

            interfaceDecl.addMember(interfaceMethod);
        }

        return interfaceCu;
    }

    /**
     * Сохраняет интерфейс в файл
     */
    private void saveInterface(CompilationUnit interfaceCu, String interfaceName, Path outputPath) throws IOException {
        // Создаем директорию для пакета
        Path packagePath = outputPath;
        if (!interfacePackage.isEmpty()) {
            packagePath = outputPath.resolve(interfacePackage.replace(".", File.separator));
            Files.createDirectories(packagePath);
        }

        // Генерируем код интерфейса
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        String interfaceCode = printer.print(interfaceCu);

        // Записываем файл
        Path interfaceFile = packagePath.resolve(interfaceName + ".java");
        Files.writeString(interfaceFile, interfaceCode);

        getLog().info("Интерфейс сохранен: " + interfaceFile);
    }
}
