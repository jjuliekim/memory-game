module me.julie.memorygame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens me.julie.memorygame to javafx.fxml;
    exports me.julie.memorygame;
}