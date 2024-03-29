package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.server.controllers.ViewClientController;
import com.serejs.diplom.desktop.text.container.View;
import com.serejs.diplom.desktop.ui.alerts.DeleteAlert;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.apache.http.HttpException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserViewController extends TableViewController<String> {
    @FXML
    private Button selectButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ListView<View> listView = new ListView<>();

    private View view = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        load();
    }

    protected void load() {
        try {
            listView.getItems().addAll(ViewClientController.getViews());
        } catch (Exception e) {
            System.err.println("Ошибка получения отображений с сервера.");
        }
    }

    @FXML
    private void select() {
        this.view = listView.getSelectionModel().getSelectedItem();
        if (this.view == null) return;

        selectButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    @FXML
    private void deleteListItem() {
        if (!DeleteAlert.confirm()) return;

        try {
            ViewClientController.deleteView(view);
            listView.getItems().remove(view);
        } catch (HttpException | IOException | URISyntaxException e) {
            ErrorAlert.info("Ошибка удаления отображения");
            e.printStackTrace();
            return;
        }

        this.view = null;
        selectButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    private void goNextPage() {
        State.setView(view);
        anotherPage(selectButton, "project-overview.fxml");
    }

    @FXML
    private void openModal() {
        openModal("modal-view.fxml");
    }

    @Override
    public void addRow(String string) {
        try {
            var view = ViewClientController.sendView(string);
            listView.getItems().add(view);
        } catch (Exception e) {
            ErrorAlert.info("Ошибка добавления изображения");
            e.printStackTrace();
        }

        modal.close();
    }
}