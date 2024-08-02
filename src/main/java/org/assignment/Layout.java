package org.assignment;

import java.awt.*;
import javax.swing.*;

public class Layout {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("su-pa-ma-ketto inuentori-");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800); // sets x,y dimension of frame
        frame.setVisible(true);


        ImageIcon image = new ImageIcon("src/main/resources/images/icon.png"); // Group Logo


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Set BorderLayout as the layout manager
        frame.setLayout(new BorderLayout());

        // For Navigation List
        // Create the West Panel
        JPanel westPanel = new JPanel();
        westPanel.setBackground(Color.ORANGE);
        westPanel.setPreferredSize(new Dimension(175, 0));
        frame.add(westPanel, BorderLayout.WEST);

        // Create a panel to handle the North, Center, East, and South panels
        JPanel mainPanel = new JPanel(new BorderLayout());


        // Top is for Headers
        // North Panel
        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.BLUE);
        northPanel.setPreferredSize(new Dimension(0, 75));
        frame.add(northPanel, BorderLayout.NORTH);

        // East Panel
        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(Color.YELLOW);
        eastPanel.setPreferredSize(new Dimension(100, 0));
        mainPanel.add(eastPanel, BorderLayout.EAST);

        // South Panel
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.YELLOW);
        southPanel.setPreferredSize(new Dimension(0, 50));
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.YELLOW);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add mainPanel to the center of the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Display the frame
        frame.setVisible(true);
    }
}