package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    public DrawPanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    private ScreenConverter sc = new ScreenConverter(-2, 2, 4, 4, 800, 600);

    private Line axisX = new Line(-1, 0, 1, 0);
    private Line axisY = new Line(0, -1, 0, 1);

    private ArrayList<Line> allLines = new ArrayList<>();
    private ArrayList<Triangle> allTriangles = new ArrayList<Triangle>();
    private Line newLine = null;
    private ScreenPoint prevPoint = null;


    @Override
    public void paint(Graphics g) {
        sc.sethS(getHeight());
        sc.setwS(getWidth());
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D gr = bi.createGraphics();
        gr.setColor(Color.WHITE);
        gr.fillRect(0, 0, getWidth(), getHeight());
        gr.dispose();
        PixelDrawer pd = new BufferedImagePixelDrawer(bi);
        LineDrawer ld = new DDALineDrawer(pd);
        TriangleDrawer td = new DDALineDrawer(pd);
        /**/
        drawAll(ld, td);
        /**/
        g.drawImage(bi, 0, 0, null);


    }

    private void drawAll(LineDrawer ld, TriangleDrawer td) {

        drawLine(ld, axisX);
        drawLine(ld, axisY);

        for (Line l : allLines) {
            drawTriangle( td, l);
        }
        if (newLine != null) {
            drawTriangle( td, newLine);

        }
    }


    private void drawLine(LineDrawer ld, Line l) {
        ld.drawLine(sc.r2s(l.getP1()), sc.r2s(l.getP2()));
    }

    private void drawTriangle(TriangleDrawer td, Line l) {
        td.drawTriangle(sc.r2s(l.getP1()), sc.r2s(l.getP2()));
    }


    private void getClickedLine(int x, int y) {
        int boxX = x - 2;
        int boxY = y - 2;
        double width = 2 * boxX;
        double height = 2 * boxY;

        for (Line selectedLine : allLines) {
            if (selectedLine.intersects(boxX, boxY, width, height)) {
                removeLine(selectedLine);
            }
        }
    }

    private void removeLine(Line line) {
        Iterator<Line> it = allLines.iterator();
        while (it.hasNext()) {
            Line selectedLine = it.next();
            if (selectedLine.equals(line)) {
                it.remove();
                repaint();
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        getClickedLine(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            prevPoint = new ScreenPoint(e.getX(), e.getY());
        } else if (e.getButton() == MouseEvent.BUTTON1) {
            newLine = new Line(
                    sc.s2r(new ScreenPoint(e.getX(), e.getY())),
                    sc.s2r(new ScreenPoint(e.getX(), e.getY())));
        }


    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
            prevPoint = null;
        } else if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            allLines.add(newLine);
            allTriangles.add(new Triangle(newLine.getP1(), newLine.getP2()));
            newLine = null;
        }
        if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
            newLine = null;
            repaint();
        }

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        ScreenPoint newPosition = new ScreenPoint(mouseEvent.getX(), mouseEvent.getY());
        if (prevPoint != null) {
            ScreenPoint screenDelta = new ScreenPoint(newPosition.getX() - prevPoint.getX(), newPosition.getY() - prevPoint.getY());
            RealPoint deltaReal = sc.s2r(screenDelta);
            double vectorX = deltaReal.getX() - sc.getxR();
            double vectorY = deltaReal.getY() - sc.getyR();

            sc.setxR(sc.getxR() - vectorX);
            sc.setyR(sc.getyR() - vectorY);
            prevPoint = newPosition;
            repaint();
        }
        if (newLine != null) {
            newLine.setP2(sc.s2r(newPosition));
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {// сделать масштабирование от центра
        int clicks = e.getWheelRotation();
        double scale = 1;
        double step = (clicks > 0) ? 0.9 : 1.1;
        for (int i = Math.abs(clicks); i > 0; i--) {
            scale *= step;
        }
        sc.setwR(scale * sc.getwR());
        sc.sethR(scale * sc.gethR());
        repaint();
    }

}