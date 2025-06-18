package com.distribuitedai.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===Cliente Distribuido===");
        System.out.println("1. Entrenar el modelo");
        System.out.println("2. Consultar el modelo");
        System.out.println("Seleccionar una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        String mensaje = "";
        if (opcion == 1) {
            mensaje = "ENTRENAMIENTO";
        } else if (opcion == 2) {
            System.out.println("Ingrese el ID del modelo: ");
            String modelId = scanner.nextLine().trim();
            mensaje = "CONSULTA:" + modelId;
        } else {
            System.out.println("Opción inválida.");
        }

        scanner.close();

        String host = "localhost";
        int port = 4000;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            
            System.out.println("Enviando comando ENTRENAMIENTO al servidor...");
            out.write(mensaje + "\n");
            out.flush();

            String linea;
            while ((linea = in.readLine()) != null) {
                System.out.println("Respuesta del servidor: " + linea);
            }

        } catch (IOException e) {
            System.err.println("Error al conectarse al servidor: " + e.getMessage());
        }
    }
}
