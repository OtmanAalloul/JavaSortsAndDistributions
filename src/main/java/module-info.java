module com.example.projectdatastructure {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires com.jfoenix;

    opens com.example.projectdatastructure to javafx.fxml;
    exports com.example.projectdatastructure;
    exports com.example.projectdatastructure.Presentation;
    exports com.example.projectdatastructure.helpers;
    opens com.example.projectdatastructure.Presentation to javafx.fxml;
}