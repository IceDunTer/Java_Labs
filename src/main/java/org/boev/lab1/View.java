package org.boev.lab1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class View extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(View.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 395, 245);
        stage.setTitle("Конвертер порций");
        stage.setScene(scene);
        stage.show();
    }
}
