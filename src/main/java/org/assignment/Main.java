package org.assignment;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setTitle("su-pa-ma-ketto inuentori-");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800); // sets x,y dimension of frame
        frame.setVisible(true);


        ImageIcon image = new ImageIcon("src/main/resources/images/icon.png"); // Group Logo
        frame.setIconImage(image.getImage()); // change icon of frame
        frame.getContentPane().setBackground(new Color(255,255,255)); // need change

        JLabel label = new JLabel();

        label.setBackground(Color.gray); // need change
        label.setOpaque(true); // display background color

        frame.add(label); // add to frame

        Border border = BorderFactory.createLineBorder(Color.white,3); // need change
        label.setBorder(border);
    }
}
