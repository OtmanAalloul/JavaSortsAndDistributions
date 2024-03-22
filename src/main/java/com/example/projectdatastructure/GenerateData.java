package com.example.projectdatastructure;

import java.util.Random;

public class GenerateData {
    public static Double[] generateLogisticDistributionData(int size, double mean, double variance) {
        Double[] data = new Double[size];
        Random random = new Random();
        double s = Math.sqrt(3 * variance / (Math.PI * Math.PI)); // Calcul de l'échelle à partir de la variance

        for (int i = 0; i < size; i++) {
            double p = random.nextDouble(); // Générer un nombre aléatoire p entre 0 et 1
            // Convertir chaque valeur générée en Double pour le stockage
            data[i] = mean + s * Math.log(p / (1 - p)); // Transformation inverse
        }
        return data;
    }
}
