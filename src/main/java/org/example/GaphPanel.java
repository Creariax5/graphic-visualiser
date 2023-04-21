package org.example;
import org.example.graphObject.Circle;
import org.example.tool.CsvToArray;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GaphPanel extends JPanel{

    public GaphPanel() {
        this.setPreferredSize(new Dimension(1400, 1000));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }

    public void paintComponent(Graphics graphics){
        String file = "D:\\devProjects\\PycharmProjects\\pythonProject1\\wikipedia\\brain\\csv\\data.csv";
        List<Graphics2D> listGraphics2D = new ArrayList<>();

        for (String[] row : Objects.requireNonNull(CsvToArray.catchCsv(file))) {
            super.paintComponent(graphics);
            int x = (int)(Math.random() * 1400);
            int y = (int)(Math.random() * 1000);

            Circle circle = new Circle(x, y, 1,row, 1);
            Graphics2D graphics2D = circle.createCircle(graphics);
            listGraphics2D.add(graphics2D);
        }
        for (Graphics2D graphics2D : listGraphics2D) {

        }
    }

    /*public void startGraphThread(){
        graphThread = new Thread(this);
        graphThread.start();
    }

    @Override
    public void run() {

        while (graphThread != null) {

            update();
            repaint();
        }
    }
    public void update(){

    } */

}
