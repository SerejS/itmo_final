package com.serejs.diplom.desktop.ui.controllers.pages;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.text.container.LiteratureType;
import com.serejs.diplom.desktop.ui.alerts.ErrorAlert;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.states.State;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class FilesViewController extends TableViewController<Literature> {
    @FXML
    private final String modalFileName = "modal-file.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var literatures = State.getLiteratures();
        table.getItems().addAll(literatures.stream().filter(s -> s.getSourceType() != SourceType.WEB).toList());

        var uri = new TableColumn<Literature, URI>("Название файла");
        uri.setMinWidth(300);
        uri.setCellValueFactory(new PropertyValueFactory<>("uri"));

        var sourceType = new TableColumn<Literature, SourceType>("Тип источника");
        sourceType.setMinWidth(200);
        sourceType.setCellValueFactory(new PropertyValueFactory<>("sourceType"));

        var litType = new TableColumn<Literature, LiteratureType>("Тип литературы");
        litType.setMinWidth(200);
        litType.setCellValueFactory(new PropertyValueFactory<>("litType"));

        super.initialize(modalFileName, uri, sourceType, litType);
    }

    @FXML
    private void openModal() {
        openModal(modalFileName);
    }

    @FXML
    public void onNextPage() {
        var literatures = getItems();
        if (literatures.isEmpty()) {
            ErrorAlert.info("Проект должен содержать хотя бы один источник");
            return;
        }

        State.setLiteratures(literatures);
        anotherPage(nextButton, "web-view.fxml");
    }

    @FXML
    public void onPrevPage() {
        State.setLiteratures(getItems());
        anotherPage(prevButton, "theme-view.fxml");
    }


    @FXML
    public void closeProject() {
        State.clearProject();
        anotherPage(prevButton, "project-overview.fxml");
    }
}
