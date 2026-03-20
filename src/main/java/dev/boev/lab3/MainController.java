package dev.boev.lab3;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainController {

    @FXML private StackPane imageContainer;  // Замените ImageView на StackPane в FXML
    @FXML private ImageView imageView;        // Основное изображение
    @FXML private Label positionLabel;
    @FXML private Label exifLabel;            // Новый Label для EXIF

    private List<File> files = new ArrayList<>();
    private int currentIndex = 0;
    private ImageCollection collection;       // Используем ваш Aggregate

    @FXML
    public void initialize() {
        loadResources();
        updateView(false); // false = без анимации при старте
    }

    private void loadResources() {
        String[] imageNames = {"1.jpg", "2.jpg", "3.jpg", "4.jpg", "5.jpg"};
        for (String name : imageNames) {
            try {
                java.io.InputStream inputStream = getClass().getResourceAsStream("/dev/boev/lab3/" + name);
                if (inputStream != null) {
                    File tempFile = File.createTempFile("img_", ".jpg");
                    tempFile.deleteOnExit();
                    java.nio.file.Files.copy(inputStream, tempFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    files.add(tempFile);
                    inputStream.close();
                }
            } catch (Exception e) {
                System.err.println("Не удалось загрузить: " + name);
            }
        }
        collection = new ImageCollection(files.toArray(new File[0]));
    }

    @FXML
    private void onChooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите папку с изображениями");
        File selectedDir = directoryChooser.showDialog(imageView.getScene().getWindow());
        if (selectedDir != null) {
            files.clear();
            collection = new ImageCollection(selectedDir);
            files.clear();
            for (int i = 0; i < collection.size(); i++) {
                files.add(collection.getFile(i));
            }
            currentIndex = 0;
            updateView(false);
        }
    }

    @FXML
    private void onNext() {
        if (files.isEmpty()) return;
        int oldIndex = currentIndex;
        currentIndex = collection.getNextIndex(currentIndex);
        slideTransition(oldIndex, currentIndex, true); // true = вправо
    }

    @FXML
    private void onPrevious() {
        if (files.isEmpty()) return;
        int oldIndex = currentIndex;
        currentIndex = collection.getPrevIndex(currentIndex);
        slideTransition(oldIndex, currentIndex, false); // false = влево
    }

    // Плавный сдвиг изображения
    private void slideTransition(int fromIdx, int toIdx, boolean forward) {
        if (fromIdx == toIdx) return;

        File nextFile = files.get(toIdx);
        Image nextImage = ImageLoader.loadFromFile(nextFile);
        if (nextImage == null) return;

        // Создаём временный ImageView для анимации
        ImageView nextView = new ImageView(nextImage);
        nextView.setFitWidth(800);
        nextView.setFitHeight(600);
        nextView.setPreserveRatio(true);
        nextView.setTranslateX(forward ? imageContainer.getWidth() : -imageContainer.getWidth());

        imageContainer.getChildren().add(nextView);

        TranslateTransition transition = new TranslateTransition(Duration.millis(300), nextView);
        transition.setToX(0);
        transition.setOnFinished(e -> {
            imageView.setImage(nextImage);
            imageContainer.getChildren().remove(nextView);
            updateExifInfo(nextFile);
        });
        transition.play();

        // Анимация ухода текущего изображения
        TranslateTransition outTransition = new TranslateTransition(Duration.millis(300), imageView);
        outTransition.setToX(forward ? -imageContainer.getWidth() : imageContainer.getWidth());
        outTransition.play();

        positionLabel.setText((currentIndex + 1) + " из " + files.size());
    }

    @FXML
    private void onPreview() {
        if (files.isEmpty()) return;
        int previewIdx = collection.getNextIndex(currentIndex);
        File previewFile = files.get(previewIdx);
        Image previewImage = ImageLoader.loadFromFile(previewFile);

        if (previewImage != null) {
            imageView.setOpacity(0.7);
            Image original = imageView.getImage();
            imageView.setImage(previewImage);

            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.millis(500));
            pause.setOnFinished(e -> {
                imageView.setImage(original);
                imageView.setOpacity(1.0);
            });
            pause.play();
        }
    }

    @FXML private void onReset() { currentIndex = 0; updateView(false); }
    @FXML private void onFirst() { if (!files.isEmpty()) { currentIndex = 0; updateView(true); } }
    @FXML private void onLast() { if (!files.isEmpty()) { currentIndex = files.size() - 1; updateView(true); } }

    private void updateView(boolean animate) {
        if (files.isEmpty()) {
            imageView.setImage(null);
            positionLabel.setText("0 из 0");
            exifLabel.setText("Нет изображений");
            return;
        }
        File currentFile = files.get(currentIndex);
        if (currentFile != null) {
            imageView.setImage(ImageLoader.loadFromFile(currentFile));
            updateExifInfo(currentFile);
        }
        positionLabel.setText((currentIndex + 1) + " из " + files.size());
    }

    // Обновление EXIF-информации
    private void updateExifInfo(File file) {
        if (file == null || exifLabel == null) return;
        Map<String, String> exif = collection.getExifInfo(file);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : exif.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        exifLabel.setText(sb.toString().trim());
    }
}