module com.dnaig.dnaig {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.softpro.dnaig to javafx.fxml;

    exports com.softpro.dnaig;
}