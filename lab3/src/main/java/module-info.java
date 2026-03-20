module dev.boev.lab3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.boev.lab3 to javafx.fxml;
    exports dev.boev.lab3;
}