package com.distribuitedai.Client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ConsultaGUI extends JFrame {
    private JTextField modelIdField;
    private JTextArea outputArea;

    public ConsultaGUI() {
        setTitle("Consulta de Modelo - Cliente RAFT");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 16);

        // Panel Consulta
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        TitledBorder border = BorderFactory.createTitledBorder("Consultar Modelo");
        border.setTitleFont(new Font("Arial", Font.BOLD, 18));
        panel.setBorder(border);

        modelIdField = new JTextField(25);
        modelIdField.setFont(font);
        JButton consultButton = new JButton("Consultar");
        consultButton.setFont(font);

        panel.add(new JLabel("ID Modelo:")).setFont(font);
        panel.add(modelIdField);
        panel.add(consultButton);

        // Área de salida
        outputArea = new JTextArea(8, 40);
        outputArea.setFont(new Font("Monospaced", Font.BOLD, 16));
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

        // Acción botón consultar
        consultButton.addActionListener(e -> {
            String id = modelIdField.getText().trim();
            if (!id.isEmpty()) {
                enviarMensaje("CONSULTA:" + id);
            } else {
                outputArea.setForeground(Color.RED);
                outputArea.setText("⚠️ Ingresa un ID de modelo válido.");
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

            outputArea.setForeground(Color.DARK_GRAY);
            outputArea.setText("");

            String line;
            boolean firstLine = true;

            while ((line = in.readLine()) != null) {
                if (firstLine && line.startsWith("CONSULTA_OK:")) {
                    outputArea.setForeground(Color.BLUE);
                    outputArea.append("✅ Modelo encontrado:\n\n");
                } else if (firstLine && line.startsWith("CONSULTA_FAIL")) {
                    outputArea.setForeground(Color.RED);
                }

                outputArea.append(line + "\n");
                firstLine = false;
            }

        } catch (IOException ex) {
            outputArea.setForeground(Color.RED);
            outputArea.setText("❌ Error de conexión: " + ex.getMessage());
        }
    }

}
