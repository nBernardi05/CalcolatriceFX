module com.bernardispa.calcolatricefx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.bernardispa.calcolatricefx to javafx.fxml;
    exports com.bernardispa.calcolatricefx;
}
