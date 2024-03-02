module com.example.taximetrie {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.taximetrie to javafx.fxml;
    opens com.example.taximetrie.Domain to javafx.base;
    exports com.example.taximetrie;
}