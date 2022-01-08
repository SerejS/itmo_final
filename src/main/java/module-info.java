module diplom.desktop.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    exports com.serejs.diplom.desktop.ui;
    opens com.serejs.diplom.desktop.ui to javafx.fxml;
}