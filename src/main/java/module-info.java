module com.crudmvc.javafx_mvc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.crudmvc to javafx.fxml;
    exports com.crudmvc;
}