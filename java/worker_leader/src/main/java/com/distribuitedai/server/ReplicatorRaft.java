package com.distribuitedai.server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

public class ReplicatorRaft {

    // Lista de nodos followers a replicar (diferentes al líder)
    private static final List<String> FOLLOWERS = List.of(
        "localhost:5001",
        "localhost:5002",
        "localhost:6000"
    );
    
    public static void replicarModelo(String modelId, String modelDir) {
        // String host = "localhost";
        // int port = 6000;

        File model = new File(modelDir + "/model_" + modelId + ".txt");
        if (!model.exists()) {
            System.out.println("Modelo no encontrado: " + model.getPath());
            return;
        }

        try {
            String contenido = Files.readString(model.toPath());

            for (String destino : FOLLOWERS) {
                String[] partes = destino.split(":");
                String host = partes[0];
                int port = Integer.parseInt(partes[1]);

                try (
                    Socket socket = new Socket(host, port);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
                ) {
                    System.out.println("Enviando réplica a " + destino + "...");
                    out.write("REPLICA:" + modelId + ";" + contenido.replace("\n", "\\n") + "\n");
                    out.flush();

                    String response = in.readLine();
                    System.out.println("Respuesta del follower: " + response);
                } catch (IOException e) {
                    System.err.println("Error replicando a " + destino + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer modelo para replicar: " + e.getMessage());
        }
    }
}
