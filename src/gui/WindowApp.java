package gui;

import core.DataProcessor;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class WindowApp extends Application {

    private TextArea textArea;
    private ListView<String> listView;
    private DataProcessor processor;

    @Override
    public void start(Stage primaryStage) {
        // Инициализация компонентов
        textArea = new TextArea();
        textArea.setPromptText("Введите числа, разделённые пробелами или переносами строк...");

        Button processButton = new Button("Обработать");
        listView = new ListView<>();
        listView.setPlaceholder(new Label("Результаты появятся здесь"));

        // Обработка нажатия кнопки
        processButton.setOnAction(event -> handleProcess());

        // Макет интерфейса
        VBox root = new VBox(10);
        root.getChildren().addAll(
                new Label("Введите данные:"),
                textArea,
                processButton,
                new Label("Результат:"),
                listView
        );

        // Настройка сцены и окна
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Data Processor — JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Инициализация DataProcessor с временным "файлом" (не используется напрямую)
        processor = new DataProcessor("dummy.txt"); // Имя не важно, так как мы не читаем из файла
    }

    private void handleProcess() {
        String input = textArea.getText().trim();
        if (input.isEmpty()) {
            showAlert("Ввод пуст", "Пожалуйста, введите данные.");
            return;
        }

        // Разделяем текст на строки, затем преобразуем в массив строк для processPipeline
        String[] lines = input.split("\\r?\\n"); // Поддержка Windows и Unix-переносов

        // Собираем все числа из всех строк
        StringBuilder allNumbers = new StringBuilder();
        for (String line : lines) {
            String[] tokens = line.trim().split("\\s+");
            for (String token : tokens) {
                if (!token.isEmpty()) {
                    allNumbers.append(token).append(" ");
                }
            }
        }

        String[] numberStrings = allNumbers.toString().trim().split(" ");

        // Вызов общего метода обработки
        String result = processor.processPipeline(numberStrings);

        // Отображение результата
        listView.getItems().clear();
        if (!result.isEmpty()) {
            List<String> parts = Arrays.asList(result.split("\\s+"));
            listView.getItems().addAll(parts);
        } else {
            listView.getItems().add("(результат пуст)");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Точка входа для запуска приложения
    public static void main(String[] args) {
        launch(args);
    }
}