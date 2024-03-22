package com.example.projectdatastructure;

import com.example.projectdatastructure.helpers.item;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private LineChart<Number, Number> performanceChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    private final List<Double> means = new ArrayList<>();
    private final List<Double> variances = new ArrayList<>();
    private static Map<Integer, Map<Double, Map<Double, Double[]>>> matrices = new HashMap<>();

    public void initialize() {
        meansVariancesTab.getTabs().remove(generatedData);
        meansVariancesTab.getTabs().remove(sortedData);
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
        if (size1.getText().isEmpty() || size2.getText().isEmpty() || size3.getText().isEmpty() || size4.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter all the sizes");
            alert.showAndWait();
        }
        if (means.isEmpty() || variances.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter the means and variances");
            alert.showAndWait();
        }
        else {
            meansVariancesTab.getTabs().add(generatedData);
            meansVariancesTab.getSelectionModel().select(generatedData);
            sizeValue1.setText(size1.getText());
            sizeValue2.setText(size2.getText());
            sizeValue3.setText(size3.getText());
            sizeValue4.setText(size4.getText());
            boxMeans.setItems(FXCollections.observableArrayList(means));
            boxVariances.setItems(FXCollections.observableArrayList(variances));
            List<Integer> dataSizes = List.of(Integer.parseInt(size1.getText()), Integer.parseInt(size2.getText()), Integer.parseInt(size3.getText()), Integer.parseInt(size4.getText()));
            List<Double> meansList = new ArrayList<>(means); // Convert means array to List<Double>
            List<Double> variancesList = new ArrayList<>(variances); // Convert variances array to List<Double>
            MatrixGenerator matrixGenerator = new MatrixGenerator(dataSizes, meansList, variancesList);
            matrices = matrixGenerator.generateMatrices();
        }
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
    }

    public void sortingData(ActionEvent actionEvent) {
        meansVariancesTab.getTabs().add(sortedData);
        meansVariancesTab.getSelectionModel().select(sortedData);
        // Clear the chart from previous data
        performanceChart.getData().clear();

        // Go through each combination of mean and variance
        for (Double mean : means) {
            for (Double variance : variances) {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName("Mean: " + mean + " Variance: " + variance);

                // Go through each data size
                for (Integer size : matrices.keySet()) {
                    Map<Double, Map<Double, Double[]>> meanMap = matrices.get(size);
                    if (meanMap != null && meanMap.containsKey(mean)) {
                        Map<Double, Double[]> varianceMap = meanMap.get(mean);
                        if (varianceMap != null && varianceMap.containsKey(variance)) {
                            Double[] unsortedData = varianceMap.get(variance);
                            // Timing the sorting
                            long sortingStartTime = System.currentTimeMillis();
                            Comparable[] sortedData = MergeSort.sortAndReturn(unsortedData);
                            long sortingEndTime = System.currentTimeMillis();

                            // Calculating time
                            long sortingTime = sortingEndTime - sortingStartTime;

                            // Add the timing data to the series
                            series.getData().add(new XYChart.Data<>(size, sortingTime));
                        }
                    }
                }
                performanceChart.getData().add(series); // Add the series to the chart
            }
        }
        // Optional: customize the chart, like adding a title or changing the axis labels
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
                Double[] data = matrices.get(size).get(mean).get(variance);
                populateTable(tableView, data);
            }
        }
    }
    private TableView<item> getTableViewForSize(int size) {
        switch (size) {
            case 10:
                return tableView1;
            case 20:
                return tableView2;
            case 30:
                return tableView3;
            case 40:
                return tableView4;
            default:
                return null;
        }
    }


}
