module diplom.desktop.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires morph;
    requires russian;
    requires epublib.core;
    requires org.jsoup;
    requires org.json;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.commons.io;
    requires org.apache.httpcomponents.httpmime;
    requires org.apache.commons.codec;
    requires markdowngenerator;
    requires java.desktop;
    requires itextpdf;
    requires static lombok;
    requires com.google.gson;
    requires java.sql;

    exports com.serejs.diplom.desktop.enums to com.google.gson;
    exports com.serejs.diplom.desktop.ui;
    opens com.serejs.diplom.desktop.ui to javafx.fxml;
    opens com.serejs.diplom.desktop.text.container;
    opens com.serejs.diplom.desktop.utils;
    exports com.serejs.diplom.desktop.ui.controllers;
    opens com.serejs.diplom.desktop.ui.controllers to javafx.fxml;
    exports com.serejs.diplom.desktop.ui.controllers.modals;
    opens com.serejs.diplom.desktop.ui.controllers.modals to javafx.fxml;
    exports com.serejs.diplom.desktop.ui.controllers.abstracts;
    opens com.serejs.diplom.desktop.ui.controllers.abstracts to javafx.fxml;
    exports com.serejs.diplom.desktop.ui.states;
    opens com.serejs.diplom.desktop.ui.states to javafx.fxml;
    exports com.serejs.diplom.desktop.ui.controllers.pages;
    opens com.serejs.diplom.desktop.ui.controllers.pages to javafx.fxml;
}