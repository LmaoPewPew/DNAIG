module com.softpro.dnaig {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxyz3d.core;
    requires org.fxyz3d.importers;

    opens com.softpro.dnaig to javafx.fxml;

    exports com.softpro.dnaig;
    exports com.softpro.dnaig.preview;
    opens com.softpro.dnaig.preview to javafx.fxml;
    exports com.softpro.dnaig.properties;
    opens com.softpro.dnaig.properties to javafx.fxml;
}