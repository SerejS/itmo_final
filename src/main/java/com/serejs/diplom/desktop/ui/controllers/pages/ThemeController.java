package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ThemeController extends TableViewController<Theme> {
    String modalFileName = "modal-theme.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var themes = State.getThemes();
        if (themes != null) table.getItems().addAll(themes);

        var title = new TableColumn<Theme, String>("Название темы");
        title.setMinWidth(200);
        title.setCellValueFactory(new PropertyValueFactory<>("title"));

        var percent = new TableColumn<Theme, Double>("Процентное содержание");
        percent.setMinWidth(200);
        percent.setCellValueFactory(new PropertyValueFactory<>("percent"));

        var root = new TableColumn<Theme, String>("Родительская тема");
        root.setMinWidth(200);
        root.setCellValueFactory(new PropertyValueFactory<>("root"));

        super.initialize(modalFileName, title, percent, root);
    }

    @FXML
    private void openModal() {
        var percent = table.getItems().stream().filter(el -> el.getRoot() == null).mapToDouble(Theme::getPercent).sum();
        if (percent >= 1) {
            ErrorAlert.info("Сумма процентов основных тем достигла предела.");
            return;
        }

        openModal(modalFileName);
    }


    @FXML
    public void deleteRow() {
        if (!DeleteAlert.confirm()) return;
        Theme t = table.getSelectionModel().getSelectedItem();
        var themes = table.getItems().filtered(item -> item == t || item.getRoot() == t);
        table.getItems().removeAll(themes);
    }


    @FXML
    private void goNextPage() {
        var themes = getItems();

        if (themes.isEmpty()) {
            ErrorAlert.info("Проект должен содержать хотя бы одну тему");
            return;
        }

        State.setThemes(themes);

        anotherPage(nextButton, "files-view.fxml");
    }


    @FXML
    private void goPrevPage() {
        State.clearProject();
        anotherPage(prevButton, "project-overview.fxml");
    }
}
