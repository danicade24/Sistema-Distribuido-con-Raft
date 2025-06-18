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

            if ("ENTRENAMIENTO".equalsIgnoreCase(request)) {
                //simulamos los datos
                double[] x = {1.0, 2.0};
                double[] y = {3.0, 4.0};
                
                String modelId = IAEngine.entrenarModelDummy(x, y);
                if (modelId != null) {
                    ReplicatorRaft.replicarModelo(modelId);
                    out.write("Entrenamiento exitoso. ID del modelo: " + modelId + "\n");
                } else {
                    out.write("Error al entrenar el modelo.\n");
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
