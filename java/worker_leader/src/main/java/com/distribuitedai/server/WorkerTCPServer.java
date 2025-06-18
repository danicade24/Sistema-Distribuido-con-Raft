package com.distribuitedai.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import com.distribuitedai.ai.IAEngine;

public class WorkerTCPServer {
    
    private static final int PORT = 5000;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("WorkerTCPServer escuchando en el puerto " + PORT);
            while (true) {
                Socket client = serverSocket.accept();
                new Thread(() -> handleClient(client)).start();                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            String request = in.readLine();
            System.out.println("Mensaje recibido: " + request);

            if (request.startsWith("ENTRENAMIENTO")) {
                double[] x = {1.0, 2.0};
                double[] y = {3.0, 4.0}; // valores por defecto

                // Intentar parsear valores si vienen incluidos
                if (request.contains(":")) {
                    try {
                        String payload = request.split(":")[1].trim(); // x1,x2;y1,y2
                        String[] partes = payload.split(";");
                        String[] xVals = partes[0].split(",");
                        String[] yVals = partes[1].split(",");

                        x[0] = Double.parseDouble(xVals[0]);
                        x[1] = Double.parseDouble(xVals[1]);
                        y[0] = Double.parseDouble(yVals[0]);
                        y[1] = Double.parseDouble(yVals[1]);
                    } catch (Exception e) {
                        System.err.println("⚠️ Error al parsear datos. Usando valores dummy.");
                    }
                }

                String modelId = IAEngine.entrenarModelDummy(x, y);
                if (modelId != null) {
                    ReplicatorRaft.replicarModelo(modelId);
                    out.write("Entrenamiento exitoso. ID del modelo: " + modelId + "\n");
                } else {
                    out.write("Error al entrenar el modelo.\n");
                }
                out.flush();
            } else if (request.startsWith("CONSULTA:")) {
                String modelId = request.split(":")[1].trim();
                File modelFile = new File("models/model_" + modelId + ".txt");

                if (modelFile.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(modelFile));
                    StringBuilder contenido = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        contenido.append(line).append("\n");
                    }
                    reader.close();
                    out.write("CONSULTA_OK:\n" + contenido + "\n");
                } else {
                    out.write("CONSULTA_FAIL: Modelo no encontrado\n");
                }
                out.flush();
            } else {
                out.write("COMANDO_DESCONOCIDO\n");
                out.flush();
            }

        } catch (IOException e) {
            System.err.println("Error procesando cliente: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}
