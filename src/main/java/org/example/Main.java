package org.example;

import org.example.graphObject.Circle;
import org.example.tool.CsvToArray;

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

                String file = "D:\\devProjects\\PycharmProjects\\scraping\\wikipedia\\brain\\csv\\data.csv";
                int i = 0;
                for (String[] row : Objects.requireNonNull(CsvToArray.catchCsv(file))) {
                    addNewElement(row, i);
                    i++;
                }
                for (int j = 0; j < i; j++) {
                    addCircleAtForeground(j);
                }
            }
        };
        Timer timer = new Timer(50, listener);
        timer.start();
    }

    private void addCircleAtForeground(int id) {


        Graphics g = surface.getGraphics();
        Circle cell = cells.get(id);

        int intX = (int) cell.getX();
        int intY = (int) cell.getY();
        int size = (int) cell.getSize();

        drawNode(intX, intY, size, g);
    }

    public void addNewElement(String[] row, int id) {
        double force = 0.1;

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

            int size = 1;

            cells.put(id, new Circle(x, y, size, row, id));
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
            double puissance = Math.pow(distanceToC/100, 1.15);
            if (distanceToC>500) {
                puissance = 100;
            }

            if (distanceToC > 300) {
                calcX = (puissance * force * calcX) / distanceToC;
                calcY = (puissance * force * calcY) / distanceToC;

                x = x - calcX;
                y = y - calcY;
            }

            double FcalcX = 0;
            double FcalcY = 0;
            int k = 0;

            int size = 1;

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


                    puissance = -(1 / (1 + Math.exp(-(distanceToNew - 0) / 80)) - 1);

                    //System.out.println(distanceX +" "+distanceY);
                    if (distanceToNew < 60) {
                        FcalcX = FcalcX + (newCell.getSize() * 160 * puissance * distanceX) / distanceToNew;
                        FcalcY = FcalcY + (newCell.getSize() * 160 * puissance * distanceY) / distanceToNew;
                    }

                    puissance = Math.pow(distanceToNew, 1);

                    for (String link : cell.getRow()) {
                        if (Objects.equals(link, newCell.getRow()[0])) {
                            FcalcX = FcalcX - (newCell.getSize() * 0.01 * puissance * distanceX) / distanceToNew;
                            FcalcY = FcalcY - (newCell.getSize() * 0.01 * puissance * distanceY) / distanceToNew;
                            Graphics g = surface.getGraphics();
                            drawLink((int) cell.getX(), (int) cell.getY(), (int) newCell.getX(), (int) newCell.getY(), g);
                            size = size + 1;
                        }
                    }
                }
                k = k + 1;
            }
            FcalcX = FcalcX / k * force;
            FcalcY = FcalcY / k * force;
            x = x + FcalcX;
            y = y + FcalcY;

            if (Math.pow(x - Xbase, 2) > 0 || Math.pow(y - Ybase, 2) > 0) {
                cells.put(id, new Circle(x, y, size, row, id));
            } else {
                x = Xbase;
                y = Ybase;
            }


        }

        int intX = (int) x;
        int intY = (int) y;

        Graphics g = surface.getGraphics();
        // drawNode(intX, intY, g);
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

    public void drawNode(int x, int y, int size, Graphics g) {
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
