package com.distribuitedai.Client;

import java.io.*;
import java.net.Socket;

public class ClientTCP {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            
            System.out.println("Enviando comando ENTRENAMIENTO al servidor...");
            out.write("ENTRENAMIENTO\n");
            out.flush();

            String response = in.readLine();
            System.out.println("Respuesta del servidor: " + response);

        } catch (IOException e) {
            System.err.println("Error al conectarse al servidor: " + e.getMessage());
        }
    }
}
