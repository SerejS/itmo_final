package com.serejs.diplom.desktop.ui.controllers.abstracts;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class RootController {
    protected void anotherPage(ButtonBase pageButton, String fileName) {
        try {
            Stage stage = (Stage) pageButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/pages/" + fileName));
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
