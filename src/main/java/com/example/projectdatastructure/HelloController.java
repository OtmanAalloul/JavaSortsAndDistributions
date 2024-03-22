package com.example.projectdatastructure;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class HelloController extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Data Generation and Sorting Performance");

        // Assuming dataSize contains distinct, ordered values for simplicity in setting tick unit
        int[] dataSize = {1000, 2000, 3000, 10000, 100000};

        // Axes - setting lower bound, upper bound, and tick unit
        final NumberAxis xAxis = new NumberAxis(dataSize[0], dataSize[dataSize.length - 1], dataSize[1] - dataSize[0]);
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Data Size");
        yAxis.setLabel("Time (Milliseconds)");
        xAxis.setAutoRanging(false); // Disable auto-ranging
        xAxis.setTickLabelsVisible(true);
        xAxis.setTickMarkVisible(true);
        xAxis.setMinorTickVisible(false); // Hide minor ticks for clarity

        // LineChart
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Performance Analysis");

        // Series for Generation Time and Sorting Time
        XYChart.Series seriesGeneration = new XYChart.Series();
        seriesGeneration.setName("Generation Time");
        XYChart.Series seriesSorting = new XYChart.Series();
        seriesSorting.setName("Sorting Time");

        double mean = 5.0;
        double variance = 16.0;

        for (int size : dataSize) {
            long generationStartTime = System.currentTimeMillis();
            Double[] data = GenerateData.generateLogisticDistributionData(size, mean, variance);
            long generationEndTime = System.currentTimeMillis();

            long sortingStartTime = System.currentTimeMillis();
            Comparable[] sortedData = MergeSort.sortAndReturn(data);
            long sortingEndTime = System.currentTimeMillis();

            // Calculating times
            long generationTime = generationEndTime - generationStartTime;
            long sortingTime = sortingEndTime - sortingStartTime;

            // Adding data to series
            seriesGeneration.getData().add(new XYChart.Data(size, generationTime));
            seriesSorting.getData().add(new XYChart.Data(size, sortingTime));
        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().addAll(seriesGeneration, seriesSorting);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
