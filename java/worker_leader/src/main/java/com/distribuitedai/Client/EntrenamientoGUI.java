package com.distribuitedai.Client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class EntrenamientoGUI extends JFrame {
    private JTextField x1Field, x2Field, y1Field, y2Field;
    private JTextArea outputArea;

    public EntrenamientoGUI() {
        setTitle("Entrenamiento de Modelo - Cliente RAFT");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 16);

        // Panel Entrenamiento
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        TitledBorder border = BorderFactory.createTitledBorder("Entrenamiento de Modelo");
        border.setTitleFont(new Font("Arial", Font.BOLD, 18));
        panel.setBorder(border);

        x1Field = new JTextField(); x1Field.setFont(font);
        x2Field = new JTextField(); x2Field.setFont(font);
        y1Field = new JTextField(); y1Field.setFont(font);
        y2Field = new JTextField(); y2Field.setFont(font);
        JButton trainButton = new JButton("Entrenar"); trainButton.setFont(font);

        panel.add(new JLabel("x1:", JLabel.RIGHT)).setFont(font);
        panel.add(x1Field);
        panel.add(new JLabel("x2:", JLabel.RIGHT)).setFont(font);
        panel.add(x2Field);
        panel.add(new JLabel("y1:", JLabel.RIGHT)).setFont(font);
        panel.add(y1Field);
        panel.add(new JLabel("y2:", JLabel.RIGHT)).setFont(font);
        panel.add(y2Field);
        panel.add(new JLabel(""));
        panel.add(trainButton);

        // Área de salida
        outputArea = new JTextArea(8, 50);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        outputArea.setForeground(Color.DARK_GRAY);
        outputArea.setBackground(new Color(245, 245, 245));
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        // scrollPane.setBorder(BorderFactory.createTitledBorder("Salida del Servidor"));
        TitledBorder outputBorder = BorderFactory.createTitledBorder("Salida del Servidor");
        outputBorder.setTitleFont(new Font("Arial", Font.BOLD, 18)); // Tamaño ajustable
        scrollPane.setBorder(outputBorder);


        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("<- Volver al Menú");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        bottomPanel.add(backButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.CENTER);


        // Acción botón entrenar
        trainButton.addActionListener(e -> {
            try {
                double x1 = Double.parseDouble(x1Field.getText().trim());
                double x2 = Double.parseDouble(x2Field.getText().trim());
                double y1 = Double.parseDouble(y1Field.getText().trim());
                double y2 = Double.parseDouble(y2Field.getText().trim());

                String mensaje = String.format("ENTRENAMIENTO:%.3f,%.3f;%.3f,%.3f", x1, x2, y1, y2);
                outputArea.setForeground(Color.BLACK); // Por si venía de error anterior
                enviarMensaje(mensaje);

            } catch (NumberFormatException ex) {
                outputArea.setForeground(Color.RED);
                outputArea.setText("❌ Error: ingresa números válidos en todos los campos.");
            }
        });


        // Acción botón volver
        backButton.addActionListener(e -> {
            new MainGUI(); // Abre el menú principal
            dispose();     // Cierra esta ventana
        });


        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void enviarMensaje(String mensaje) {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.write(mensaje + "\n");
            out.flush();

            outputArea.setText("");
            String line;
            while ((line = in.readLine()) != null) {
                outputArea.append(line + "\n");
            }

        } catch (IOException ex) {
            outputArea.setForeground(Color.RED);
            outputArea.setText("❌ Error de conexión: " + ex.getMessage());
        }
    }
}
