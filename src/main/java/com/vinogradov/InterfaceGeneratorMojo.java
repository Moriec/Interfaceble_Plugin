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
import java.util.List;
import java.util.Optional;

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
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/interfaces")
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
        String interfaceName = className + interfaceSuffix;

        getLog().info("Генерируем интерфейс " + interfaceName + " для класса " + className);

        // Создаем новый CompilationUnit для интерфейса
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
        interfaceDecl.setJavadocComment("Автоматически сгенерированный интерфейс для класса " + className);

        // Находим все публичные методы в классе
        List<MethodDeclaration> publicMethods = classDecl.findAll(MethodDeclaration.class)
                .stream()
                .filter(method -> method.isPublic() && !method.isStatic())
                .toList();

        if (publicMethods.isEmpty()) {
            getLog().info("В классе " + className + " не найдено публичных методов для генерации интерфейса");
            return;
        }

            // Добавляем методы в интерфейс
            for (MethodDeclaration method : publicMethods) {
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
