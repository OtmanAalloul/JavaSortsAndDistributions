package com.example.projectdatastructure;

import com.example.projectdatastructure.helpers.DataInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrixGenerator {
    private List<Integer> dataSizes; // Tailles d'ensembles de données
    private List<Double> means;      // Moyennes
    private List<Double> variances;  // Variances

    public MatrixGenerator(List<Integer> dataSizes, List<Double> means, List<Double> variances) {
        this.dataSizes = dataSizes;
        this.means = means;
        this.variances = variances;
    }

    public Map<Integer, Map<Double, Map<Double, DataInfo>>> generateMatrices() {
        Map<Integer, Map<Double, Map<Double, DataInfo>>> matrices = new HashMap<>();

        for (int size : dataSizes) {
            Map<Double, Map<Double, DataInfo>> matrixForMean = new HashMap<>();

            for (double mean : means) {
                Map<Double, DataInfo> matrixForVariance = new HashMap<>();

                for (double variance : variances) {
                    long startTime = System.nanoTime();
                    Double[] samples = GenerateData.generateLogisticDistributionData(size, mean, variance);
                    long generationTime = (System.nanoTime() - startTime) / 1000000;

                    // Here we should sort data to calculate sortingTime
                    Double[] sortedSamples = MergeSort.sortAndReturn(samples); // Ensure this returns Double[]
                    long sortingTime = (System.nanoTime() - startTime) / 1000000 - generationTime;

                    DataInfo dataInfo = new DataInfo(samples, generationTime, sortingTime);
                    matrixForVariance.put(variance, dataInfo);
                }
                matrixForMean.put(mean, matrixForVariance);
            }
            matrices.put(size, matrixForMean);
        }
        return matrices;
    }
}
