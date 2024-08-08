module com.crudmvc.javafx_mvc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;

    opens com.crudmvc.Models.domain to javafx.base, javafx.fxml;
    opens com.crudmvc.Controllers to javafx.fxml;


    exports com.crudmvc.Models.domain;
    exports com.crudmvc.Controllers;
    exports com.crudmvc to javafx.graphics;

}
