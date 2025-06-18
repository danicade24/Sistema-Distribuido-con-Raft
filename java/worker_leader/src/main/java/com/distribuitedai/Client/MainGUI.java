package com.distribuitedai.Client;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    public MainGUI() {
        setTitle("Cliente RAFT Distribuido");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));
        setLocationRelativeTo(null);

        JLabel title = new JLabel("¿Qué deseas hacer?", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnEntrenar = new JButton("Entrenar Modelo");
        btnEntrenar.setFont(new Font("Arial", Font.BOLD, 18));
        btnEntrenar.setPreferredSize(new Dimension(200, 50));

        JButton btnConsultar = new JButton("Consultar Modelo");
        btnConsultar.setFont(new Font("Arial", Font.BOLD, 18));
        btnConsultar.setPreferredSize(new Dimension(200, 50));


        btnEntrenar.setFont(new Font("Arial", Font.BOLD, 16));
        btnConsultar.setFont(new Font("Arial", Font.BOLD, 16));

        btnEntrenar.addActionListener(e -> {
            new EntrenamientoGUI();
            dispose();
        });

        btnConsultar.addActionListener(e -> {
            new ConsultaGUI();
            dispose();
        });

        add(title);
        add(btnEntrenar);
        add(btnConsultar);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}
