package com.distribuitedai.ai;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class IAEngine {
    private static final String MODELS_DIR = "models";

    public static String entrenarModelDummy(double[] x, double[] y) {
        // simulamos una regresion lineal
        double m = (y[1] - y[0]) / (x[1] - x[0]); // pendiente
        double b = y[0] - m * x[0]; // intercepto
        
        String modelId = UUID.randomUUID().toString().trim();
        String filename = MODELS_DIR + "/model_" + modelId + ".txt";

        try {
            File dir = new File(MODELS_DIR);
            if (!dir.exists()) dir.mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write("Modelo Dummy - Regresion Lineal\n");
            writer.write("Pendiente: " + m + "\n");
            writer.write("Intercepto: " + b + "\n");
            writer.close();

            System.out.println("Modelo entrenado y guardado: " + filename);
            return modelId;
        } catch (IOException e) {
            System.err.println("Error al guardar el modelo: " + e.getMessage());
            return null;
        }
    }
}
