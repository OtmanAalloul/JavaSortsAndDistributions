package com.example.projectdatastructure.helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataExporter {

    public static void exportData(String directoryPath, int dataSize, double mean, double variance, Double[] data, long generationTime, long sortingTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = String.format("results_datasize_%d_mean_%.2f_variance_%.2f_%s.txt", dataSize, mean, variance, timestamp);

        try (FileWriter writer = new FileWriter(new File(directoryPath, fileName))) {
            writer.write("Algorithm: Merge Sort\n");
            writer.write("Distribution: Logistic\n");
            writer.write(String.format("Parameters: mean = %.2f, variance = %.2f, dataSize = %d\n", mean, variance, dataSize));
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("Time for generating: " + generationTime + " ms\n");
            writer.write("Time for sorting: " + sortingTime + " ms\n");
            writer.write("GeneratedData: \n");
            for (double value : data) {
                writer.write(value + "\n");
            }
            // Add any additional metadata like JVM warm-up rounds if needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
