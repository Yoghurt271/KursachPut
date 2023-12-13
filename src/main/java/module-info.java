module com.example.kursachput {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.apache.poi.poi;


    opens com.example.kursachput to javafx.fxml;
    exports com.example.kursachput;
}