package com.distribuitedai.server;

import java.io.*;
import java.net.Socket;

public class ReplicatorRaft {
    
    public static void replicarModelo(String modelId) {
        String host = "localhost";
        int port = 6000;

        File model = new File("models/model_" + modelId + ".txt");
        if (!model.exists()) {
            System.out.println("Modelo no encontrado: " + modelId);
            return;
        }
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader modelReader = new BufferedReader(new FileReader(model))) {
            
            System.out.println("Enviando comando REPLICACION al servidor...");
            out.write("REPLICACION: " + modelId + "\n");
            out.flush();

            //enviamos contenido para el archivo
            String line;
            while ((line = modelReader.readLine()) != null) {
                out.write(line + "\n");
            }
            out.flush();

            String response = in.readLine();
            System.out.println("Respuesta del follower: " + response);

        } catch (IOException e) {
            System.err.println("Error al replicar el modelo: " + e.getMessage());
        }
    }
}
