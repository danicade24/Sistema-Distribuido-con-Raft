package com.distribuitedai.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class NodoCoordinadorTCP {
    private static final int PORT = 4000;
    private static final List<String> WORKERS = Arrays.asList(
        "localhost:5000",  // Worker líder
        "localhost:5001",  // Follower 1
        "localhost:5002"   // Follower 2
    );
    private static int workerIndex = 0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Nodo Coordinador escuchando en puerto " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            String request = in.readLine();
            System.out.println("Recibido del cliente: " + request);

            // Reenvío al siguiente worker en Round-Robin
            String worker = WORKERS.get(workerIndex % WORKERS.size());
            workerIndex++;
            String[] parts = worker.split(":");
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);

            try (
                Socket workerSocket = new Socket(host, port);
                BufferedWriter workerOut = new BufferedWriter(new OutputStreamWriter(workerSocket.getOutputStream()));
                BufferedReader workerIn = new BufferedReader(new InputStreamReader(workerSocket.getInputStream()))
            ) {
                workerOut.write(request + "\n");
                workerOut.flush();

                String responseLine;
                StringBuilder response = new StringBuilder();
                while ((responseLine = workerIn.readLine()) != null) {
                    response.append(responseLine).append("\n");
                }

                out.write(response.toString());
                out.flush();

            } catch (IOException ex) {
                System.err.println("Error comunicando con worker " + worker + ": " + ex.getMessage());
                out.write("ERROR: No se pudo comunicar con el worker.\n");
                out.flush();
            }

        } catch (IOException e) {
            System.err.println("Error en cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }
}
