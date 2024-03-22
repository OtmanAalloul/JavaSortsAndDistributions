package com.example.projectdatastructure;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DepotageController implements Initializable {
    @FXML
    private AnchorPane depotage;

    @FXML
    private JFXTabPane depotageTab;

    @FXML
    private Tab expedie;

    @FXML
    private Tab recu;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
