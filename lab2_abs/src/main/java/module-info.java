module dev.boev.lab2_new {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.boev.lab2_new to javafx.fxml;
    exports dev.boev.lab2_new;
}