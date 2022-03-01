package com.serejs.diplom.desktop.ui.controllers.modals;

import com.serejs.diplom.desktop.text.container.Theme;
import com.serejs.diplom.desktop.ui.controllers.abstracts.ModalController;
import com.serejs.diplom.desktop.ui.controllers.abstracts.TableViewController;
import com.serejs.diplom.desktop.ui.controllers.ThemeController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class ModalThemeController extends ModalController<Theme> {
    @FXML
    private TextField titleTheme;
    @FXML
    private TextArea textArea;
    @FXML
    private Slider slider;
    @FXML
    private ComboBox<Theme> themeBox;

    @Override
    public void setParent(TableViewController<Theme> parent) {
        this.parent = parent;
        if (themeBox != null && parent instanceof ThemeController controller) {
            themeBox.getItems().addAll(controller.getItems());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (textArea != null) textArea.setWrapText(true);
    }

    public void addTheme() {
        if (parent == null) return;

        Set<String> keyWords = Arrays.stream(textArea.getText().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());


        if (obj == null) {
            obj = new Theme(themeBox.getValue(), titleTheme.getText(), slider.getValue(), keyWords);
            parent.addRow(obj);
            return;
        }

        obj.setRoot(themeBox.getValue());
        obj.setTitle(titleTheme.getText());
        obj.setPercent(slider.getValue() / 100.);
        obj.setKeyWords(keyWords);

        parent.updateRows();
    }

    @Override
    public void setObject(Theme theme) {
        this.obj = theme;

        var keywords = new StringBuilder();
        theme.getKeyWords().forEach(word -> keywords.append(word).append(", "));

        titleTheme.setText(theme.getTitle());
        textArea.setText(keywords.toString());
        slider.setValue(theme.getPercent() * 100);
        if (theme.getRoot() != null) themeBox.setValue(theme.getRoot());
    }
}
