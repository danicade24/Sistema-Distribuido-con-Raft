package com.distribuitedai.Client;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClienteGUI extends JFrame {
    private JTextField x1Field, x2Field, y1Field, y2Field, modelIdField;
    private JTextArea outputArea;

    public ClienteGUI() {
        setTitle("Cliente RAFT Distribuido");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font fontLabel = new Font("Arial", Font.BOLD, 16);
        Font fontField = new Font("Arial", Font.PLAIN, 16);
        Font fontButton = new Font("Arial", Font.BOLD, 16);

        // Panel de entrenamiento
        JPanel trainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        // trainPanel.setBorder(BorderFactory.createTitledBorder("Entrenamiento de Modelo"));
        TitledBorder border = BorderFactory.createTitledBorder("Entrenamiento de Modelo");
        border.setTitleFont(new Font("Arial", Font.BOLD, 18));
        trainPanel.setBorder(border);


        x1Field = new JTextField(); x1Field.setFont(fontField);
        x2Field = new JTextField(); x2Field.setFont(fontField);
        y1Field = new JTextField(); y1Field.setFont(fontField);
        y2Field = new JTextField(); y2Field.setFont(fontField);
        JButton trainButton = new JButton("Entrenar"); trainButton.setFont(fontButton);

        trainPanel.add(new JLabel("x1:", JLabel.RIGHT)).setFont(fontLabel);
        trainPanel.add(x1Field);
        trainPanel.add(new JLabel("x2:", JLabel.RIGHT)).setFont(fontLabel);
        trainPanel.add(x2Field);
        trainPanel.add(new JLabel("y1:", JLabel.RIGHT)).setFont(fontLabel);
        trainPanel.add(y1Field);
        trainPanel.add(new JLabel("y2:", JLabel.RIGHT)).setFont(fontLabel);
        trainPanel.add(y2Field);
        trainPanel.add(new JLabel(""));
        trainPanel.add(trainButton);

        // Panel de consulta
        JPanel consultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // consultPanel.setBorder(BorderFactory.createTitledBorder("Consultar Modelo"));
        TitledBorder consultBorder = BorderFactory.createTitledBorder("Consultar Modelo");
        consultBorder.setTitleFont(new Font("Arial", Font.BOLD, 18));
        consultBorder.setTitleColor(Color.DARK_GRAY); // Opcional
        consultPanel.setBorder(consultBorder);

        modelIdField = new JTextField(20); modelIdField.setFont(fontField);
        JButton consultButton = new JButton("Consultar"); consultButton.setFont(fontButton);

        consultPanel.add(new JLabel("ID Modelo:")).setFont(fontLabel);
        consultPanel.add(modelIdField);
        consultPanel.add(consultButton);

        // Área de salida
        outputArea = new JTextArea(10, 40);
        outputArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        outputArea.setEditable(false);
        outputArea.setForeground(Color.RED);    //solo para colorear el texto
        outputArea.setBackground(new Color(245, 245, 245)); // gris claro

        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Añadir componentes a la ventana
        add(trainPanel, BorderLayout.NORTH);
        add(consultPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Acción botón ENTRENAR
        trainButton.addActionListener(e -> {
            String msg = "ENTRENAMIENTO";
            enviarMensaje(msg);
        });

        // Acción botón CONSULTAR
        consultButton.addActionListener(e -> {
            String id = modelIdField.getText().trim();
            if (!id.isEmpty()) {
                String msg = "CONSULTA:" + id;
                enviarMensaje(msg);
            }
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
            outputArea.setText("❌ Error de conexión: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClienteGUI::new);
    }
}
