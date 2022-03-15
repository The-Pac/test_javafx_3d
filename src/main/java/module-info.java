module com.example.test_3d {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.test_3d to javafx.fxml;
    exports com.example.test_3d;
}