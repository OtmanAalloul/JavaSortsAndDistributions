package com.example.projectdatastructure;

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

    public Map<Integer, Map<Double, Map<Double, Double[]>>> generateMatrices() {
        Map<Integer, Map<Double, Map<Double, Double[]>>> matrices = new HashMap<>();

        // Pour chaque taille d'ensemble de données, créez une matrice
        for (int size : dataSizes) {
            // Générez une matrice 2D pour la taille actuelle
            Map<Double, Map<Double, Double[]>> matrix = new HashMap<>();

            // Pour chaque moyenne
            for (double mean : means) {
                Map<Double, Double[]> row = new HashMap<>();

                // Pour chaque variance
                for (double variance : variances) {
                    // Générez des données pour la combinaison de moyenne et variance
                    Double[] samples = GenerateData.generateLogisticDistributionData(size, mean, variance);
                    row.put(variance, samples);
                }

                matrix.put(mean, row);
            }

            // Associez la matrice générée à la taille correspondante
            matrices.put(size, matrix);
        }
        return matrices;
    }
}

