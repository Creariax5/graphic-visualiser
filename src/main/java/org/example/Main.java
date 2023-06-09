package org.example;

import org.example.graphObject.Circle;
import org.example.tool.CsvToArray;
import org.example.tool.Vector;

import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

public class Main {
    JLabel view;
    BufferedImage surface;
    Random random = new Random();

    Hashtable<Integer, Circle> cells = new Hashtable<>();
    Hashtable<String, Integer> IdWithName = new Hashtable<>();

    List<Integer> listCircle = new ArrayList<>();

    public Main()
    {
        surface = new BufferedImage(1400,1000,BufferedImage.TYPE_INT_RGB);
        view = new JLabel(new ImageIcon(surface));

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Graphics g = surface.getGraphics();
                g.setColor(Color.BLACK);
                g.fillRect(0,0,1400,1000);
                g.dispose();

                String file = "D:\\devProjects\\PycharmProjects\\pythonProject1\\wikipedia\\brain\\csv\\data.csv";
                int i = 0;
                for (String[] row : Objects.requireNonNull(CsvToArray.catchCsv(file))) {
                    addNewElement(row, i);
                    i++;
                }
            }
        };
        Timer timer = new Timer(50, listener);
        timer.start();
    }

    public void addNewElement(String[] row, int id) {
        int force = 1;

        double x;
        double y;

        double calcX;
        double calcY;
        double Xbase = 0;
        double Ybase = 0;
        if (id >= listCircle.size()) {

            x = random.nextDouble(1400);
            y = random.nextDouble(1000);

            listCircle.add(id);

            cells.put(id, new Circle(x, y, row, id));
            IdWithName.put(cells.get(id).getRow()[0], id);
            System.out.println(cells.get(id).getRow()[0] + " " + id);

        } else {
            Circle cell = cells.get(id);

            x = cell.getX();
            y = cell.getY();
            Xbase = x;
            Ybase = y;

            calcX = x - 700;
            calcY = y - 500;

            double distanceToC = Math.sqrt(Math.pow(calcX, 2) + Math.pow(calcY, 2));

            if (distanceToC > 10) {
                calcX = (1 * force * calcX) / distanceToC;
                calcY = (1 * force * calcY) / distanceToC;

                x = x - calcX;
                y = y - calcY;
            }

            double FcalcX = 0;
            double FcalcY = 0;
            int k = 0;

            for (int j : listCircle) {
                calcX = x - 700;
                calcY = y - 500;

                if (j != id) {

                    Circle newCell = cells.get(j);
                    double newX = newCell.getX() - 700;
                    double newY = newCell.getY() - 500;

                    double distanceToNew = Math.sqrt(Math.pow(newX - calcX, 2) + Math.pow(newY - calcY, 2));

                    double distanceX = calcX - newX;
                    double distanceY = calcY - newY;


                    double puissance = -(1 / (1 + Math.exp(-(distanceToNew - 0) / 80)) - 1);

                    //System.out.println(distanceX +" "+distanceY);
                    if (distanceToNew < 120) {
                        FcalcX = FcalcX + (120 * puissance * distanceX) / distanceToNew;
                        FcalcY = FcalcY + (120 * puissance * distanceY) / distanceToNew;
                    }

                    puissance = Math.pow(distanceToNew, 1);

                    for (String link : cell.getRow()) {
                        if (Objects.equals(link, newCell.getRow()[0])) {
                            FcalcX = FcalcX - (0.5 * puissance * distanceX) / distanceToNew;
                            FcalcY = FcalcY - (0.5 * puissance * distanceY) / distanceToNew;
                            Graphics g = surface.getGraphics();
                            drawLink((int) cell.getX(), (int) cell.getY(), (int) newCell.getX(), (int) newCell.getY(), g);
                        }
                    }
                }
                k = k + 1;
            }
            FcalcX = FcalcX / k * force;
            FcalcY = FcalcY / k * force;
            x = x + FcalcX;
            y = y + FcalcY;

            if (Math.pow(x - Xbase, 2) > 0.12 || Math.pow(y - Ybase, 2) > 0.12) {
                cells.put(id, new Circle(x, y, row, id));
            } else {
                x = Xbase;
                y = Ybase;
            }


        }

        int intX = (int) x;
        int intY = (int) y;

        Graphics g = surface.getGraphics();
        drawNode(intX, intY, g);
        drawArc((int) (intX + (x - Xbase) * 10), (int) (intY + (y - Ybase) * 10), intX, intY, g);

        g.dispose();
        view.repaint();
    }

    public static void main(String[] args)
    {
        Main canvas = new Main();
        JFrame frame = new JFrame();
        int vertexes = 0;
        // Change this next part later to be dynamic.
        vertexes = 10;
        int canvasSize = vertexes * vertexes;
        frame.setSize(canvasSize, canvasSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(canvas.view);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public void drawNode(int x, int y, Graphics g) {
        int size = 8;

        Graphics2D graphics2D = (Graphics2D) g;

        graphics2D.setColor(Color.WHITE);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.fillOval(x, y, size, size);

        // detection area
        /*graphics2D.setColor(Color.RED);
        graphics2D.drawOval(x-120, y-120, 240, 240)*/;
    }

    public void drawArc(int x, int y, int xx, int yy, Graphics g) {
        // translation vector
        Graphics2D graphics2D = (Graphics2D) g;

        graphics2D.setColor(Color.BLUE);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawLine(x + 4, y + 4, xx + 4, yy + 4);
    }

    public void drawLink(int x, int y, int xx, int yy, Graphics g) {

        Graphics2D graphics2D = (Graphics2D) g;

        graphics2D.setColor(Color.GRAY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawLine(x + 4, y + 4, xx + 4, yy + 4);
    }
}
