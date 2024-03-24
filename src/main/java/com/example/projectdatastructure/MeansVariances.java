package com.example.projectdatastructure;

import com.example.projectdatastructure.helpers.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTabPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.*;

public class MeansVariances {
    @FXML
    public JFXButton clear;
    @FXML
    public Label meansValues;
    @FXML
    public Label variancesValues;
    @FXML
    public Label sizeValue1;
    @FXML
    public Label sizeValue2;
    @FXML
    public Label sizeValue3;
    @FXML
    public Label sizeValue4;
    @FXML
    public TextField valueMeans;
    @FXML
    public TextField valueVariances;
    @FXML
    public TextField size1;
    @FXML
    public TextField size2;
    @FXML
    public TextField size3;
    @FXML
    public TextField size4;
    @FXML
    public JFXTabPane meansVariancesTab;
    @FXML
    public Tab generatedData;
    @FXML
    public Tab setUp;
    @FXML
    public Tab sortedData;
    @FXML
    public TableView<item> tableView1;
    @FXML
    public TableView<item> tableView2;
    @FXML
    public TableView<item> tableView3;
    @FXML
    public TableView<item> tableView4;
    @FXML
    public ComboBox boxMeans;
    @FXML
    public ComboBox boxVariances;
    @FXML
    public StackPane rootPane;
    @FXML
    public Tab generatedDataPerformance;
    @FXML
    private LineChart<String, Number> performanceChartGenerating;
    @FXML
    private LineChart<String, Number> performanceChart;

    private JFXSpinner spinner;

    private final List<Double> means = new ArrayList<>();
    private final List<Double> variances = new ArrayList<>();
    private final List<Integer> dataSizes = new ArrayList<>();

    private static Map<Integer, Map<Double, Map<Double, DataInfo>>> matrices = new HashMap<>();

    public void initialize() {
        meansVariancesTab.getTabs().remove(generatedData);
        meansVariancesTab.getTabs().remove(sortedData);
        meansVariancesTab.getTabs().remove(generatedDataPerformance);
        meansValues.setText("");
        variancesValues.setText("");
        valueMeans.setText("");
        valueVariances.setText("");
        size1.setText("");
        size2.setText("");
        size3.setText("");
        size4.setText("");
        boxMeans.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && boxVariances.getValue() != null) {
                Double mean = (Double) newValue;
                Double selectedVariance = (Double) boxVariances.getValue();

                // Use the matrices map to populate tables
                populateTablesFromMatrices(mean, selectedVariance);
            }
        });

        boxVariances.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && boxMeans.getValue() != null) {
                Double selectedMean = (Double) boxMeans.getValue();
                Double variance = (Double) newValue;

                // Use the matrices map to populate tables
                populateTablesFromMatrices(selectedMean, variance);
            }
        });

        // Initialize table columns just once, not in the generate method
        TableColumn<item, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<item, Double> dataColumn = new TableColumn<>("Data");
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

        tableView1.getColumns().addAll(idColumn, dataColumn);
        tableView2.getColumns().addAll(idColumn, dataColumn);
        tableView3.getColumns().addAll(idColumn, dataColumn);
        tableView4.getColumns().addAll(idColumn, dataColumn);

        spinner = new JFXSpinner();
        spinner.setMaxSize(50, 50);
        spinner.setVisible(false);
        StackPane.setAlignment(spinner, Pos.CENTER);
        rootPane.getChildren().add(spinner);
    }

    public void addMean(ActionEvent actionEvent) {
        if (meansValues.getText().isEmpty() && !valueMeans.getText().isEmpty()) {
            meansValues.setText(valueMeans.getText());
            double mean = Double.parseDouble(valueMeans.getText());
            means.add(mean);
            valueMeans.setText("");
        }
        if (valueMeans.getText().isEmpty()) {
            meansValues.setText(meansValues.getText());
        } else {
            double mean = Double.parseDouble(valueMeans.getText());
            means.add(mean);
            meansValues.setText(meansValues.getText() + " , " + valueMeans.getText());
            valueMeans.setText("");
        }
    }

    public void addVariance(ActionEvent actionEvent) {
        if (variancesValues.getText().isEmpty() && !valueVariances.getText().isEmpty()) {
            variancesValues.setText(valueVariances.getText());
            double variance = Double.parseDouble(valueVariances.getText());
            variances.add(variance);
            valueVariances.setText("");
        } else if (valueVariances.getText().isEmpty()) {
            variancesValues.setText(variancesValues.getText());
        } else {
            double variance = Double.parseDouble(valueVariances.getText());
            variances.add(variance);
            variancesValues.setText(variancesValues.getText() + " , " + valueVariances.getText());
            valueVariances.setText("");
        }
    }

    public void generate(ActionEvent actionEvent) {
        meansVariancesTab.getTabs().add(generatedDataPerformance);
        meansVariancesTab.getTabs().add(generatedData);
        meansVariancesTab.getSelectionModel().select(generatedDataPerformance);
        if (size1.getText().isEmpty() || size2.getText().isEmpty() || size3.getText().isEmpty() || size4.getText().isEmpty()) {
            showAlert("Please enter all the sizes");
            return;
        }

        if (means.isEmpty() || variances.isEmpty()) {
            showAlert("Please enter the means and variances");
            return;
        }

        toggleSpinner(true); // Show spinner

        // Clear previous data from the chart
        Platform.runLater(() -> performanceChartGenerating.getData().clear());

        // Start the data generation task
        Task<Void> generateTask = new Task<>() {
            @Override
            protected Void call() {
                sizeValue1.setText(size1.getText());
                sizeValue2.setText(size2.getText());
                sizeValue3.setText(size3.getText());
                sizeValue4.setText(size4.getText());
                boxMeans.setItems(FXCollections.observableArrayList(means));
                boxVariances.setItems(FXCollections.observableArrayList(variances));
                dataSizes.add(Integer.parseInt(size1.getText()));
                dataSizes.add(Integer.parseInt(size2.getText()));
                dataSizes.add(Integer.parseInt(size3.getText()));
                dataSizes.add(Integer.parseInt(size4.getText()));

                MatrixGenerator matrixGenerator = new MatrixGenerator(dataSizes, means, variances);
                Map<Integer, Map<Double, Map<Double, DataInfo>>> sizeMatrices = matrixGenerator.generateMatrices();

                // Update the matrices map with the newly generated data
                matrices.putAll(sizeMatrices);

                // Create a series for each mean and variance combination
                means.forEach(mean -> variances.forEach(variance -> {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName("Mean: " + mean + ", Variance: " + variance);

                    // Add data points to the series
                    dataSizes.forEach(size -> {
                        DataInfo dataInfo = sizeMatrices.get(size).get(mean).get(variance);
                        series.getData().add(new XYChart.Data<>(size.toString(), dataInfo.getGenerationTime()));
                    });

                    // Update the chart on the JavaFX thread
                    Platform.runLater(() -> performanceChartGenerating.getData().add(series));
                }));

                toggleSpinner(false); // Hide spinner after generation is done
                return null;
            }

            @Override
            protected void failed() {
                super.failed();
                showAlert("Failed to generate data: " + getException().getMessage());
                toggleSpinner(false);
            }
        };

        // Start the task on a background thread
        new Thread(generateTask).start();
    }


    public void clear(ActionEvent actionEvent) {
        meansValues.setText("");
        variancesValues.setText("");
        valueMeans.setText("");
        valueVariances.setText("");
        size1.setText("");
        size2.setText("");
        size3.setText("");
        size4.setText("");
        tableView1.getItems().clear();
        tableView2.getItems().clear();
        tableView3.getItems().clear();
        tableView4.getItems().clear();
        boxMeans.getItems().clear();
        boxVariances.getItems().clear();
        means.clear();
        variances.clear();
        matrices.clear();
        dataSizes.clear();
        meansVariancesTab.getTabs().remove(generatedData);
        meansVariancesTab.getTabs().remove(sortedData);
        meansVariancesTab.getSelectionModel().select(setUp);
        performanceChart.getData().clear();
    }

    public void sortingData(ActionEvent actionEvent) {
        meansVariancesTab.getTabs().add(sortedData);
        meansVariancesTab.getSelectionModel().select(sortedData);
        toggleSpinner(true); // Show spinner

        // Clear previous data from the chart
        Platform.runLater(() -> performanceChart.getData().clear());

        // Start the data generation task
        Task<Void> sortingTask = new Task<>() {
            @Override
            protected Void call() {
                // Create a series for each mean and variance combination
                means.forEach(mean -> variances.forEach(variance -> {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName("Mean: " + mean + ", Variance: " + variance);

                    // Add data points to the series
                    dataSizes.forEach(size -> {
                        DataInfo dataInfo = matrices.get(size).get(mean).get(variance);
                        series.getData().add(new XYChart.Data<>(size.toString(), dataInfo.getSortingTime()));
                    });

                    // Update the chart on the JavaFX thread
                    Platform.runLater(() -> performanceChart.getData().add(series));
                }));

                toggleSpinner(false); // Hide spinner after generation is done
                return null;
            }

            @Override
            protected void failed() {
                super.failed();
                showAlert("Failed to sorting data: " + getException().getMessage());
                toggleSpinner(false);
            }
        };

        // Start the task on a background thread
        new Thread(sortingTask).start();
    }



    private void populateTable(TableView<item> tableView, Double[] generatedData) {
        ObservableList<item> dataItems = FXCollections.observableArrayList();
        int id = 1;
        for (Double dataValue : generatedData) {
            dataItems.add(new item(id++, dataValue));
        }
        tableView.setItems(dataItems);
        tableView.refresh(); // Refresh the table view to update the display
    }
    private void populateTablesFromMatrices(Double mean, Double variance) {
        List<Integer> dataSizes = List.of(Integer.parseInt(size1.getText()), Integer.parseInt(size2.getText()), Integer.parseInt(size3.getText()), Integer.parseInt(size4.getText()));
        for (int size : dataSizes) {
            TableView<item> tableView = getTableViewForSize(size);
            if (tableView != null && matrices.containsKey(size) && matrices.get(size).containsKey(mean) && matrices.get(size).get(mean).containsKey(variance)) {
                DataInfo dataInfo = matrices.get(size).get(mean).get(variance);
                Double[] data = dataInfo.getData();
                populateTable(tableView, data);
            }
        }
    }

    private TableView<item> getTableViewForSize(int size) {
        if (size == dataSizes.get(0)) {
            return tableView1;
        } else if (size == dataSizes.get(1)) {
            return tableView2;
        } else if (size == dataSizes.get(2)) {
            return tableView3;
        } else if (size == dataSizes.get(3)) {
            return tableView4;
        } else {
            return null;
        }
    }
    private void updateUI() {
        // Perform all updates to the UI after data generation completes
        Platform.runLater(() -> {
            meansVariancesTab.getTabs().add(generatedData);
            sizeValue1.setText(size1.getText());
            sizeValue2.setText(size2.getText());
            sizeValue3.setText(size3.getText());
            sizeValue4.setText(size4.getText());
            boxMeans.setItems(FXCollections.observableArrayList(means));
            boxVariances.setItems(FXCollections.observableArrayList(variances));
        });
    }
    private void toggleSpinner(boolean show) {
        // If you are not already on the JavaFX thread, wrap this in Platform.runLater
        spinner.setVisible(show);
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void importData(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Export Directory");
        File selectedDirectory = directoryChooser.showDialog(rootPane.getScene().getWindow());

        if (selectedDirectory != null) {
            String directoryPath = selectedDirectory.getAbsolutePath();

            for (Integer size : matrices.keySet()) {
                for (Double mean : matrices.get(size).keySet()) {
                    for (Double variance : matrices.get(size).get(mean).keySet()) {
                        DataInfo dataInfo = matrices.get(size).get(mean).get(variance);
                        Double[] data = dataInfo.getData();
                        long generationTime = dataInfo.getGenerationTime();
                        long sortingTime = dataInfo.getSortingTime();

                        com.example.projectdatastructure.helpers.DataExporter.exportData(directoryPath, size, mean, variance, data, generationTime, sortingTime);
                    }
                }
            }
        }
    }

}