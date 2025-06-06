module com.example.comp242project1dis {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.comp242project1dis to javafx.fxml;
    exports com.example.comp242project1dis;
}