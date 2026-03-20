module org.boev.lab1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.boev.lab1 to javafx.fxml;
    exports org.boev.lab1;
}