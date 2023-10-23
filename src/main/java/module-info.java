module com.dnaig.dnaig {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.dnaig.dnaig to javafx.fxml;
    exports com.dnaig.dnaig;
}