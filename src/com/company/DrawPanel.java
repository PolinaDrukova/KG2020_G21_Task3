package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
        /**/
        //  ld.drawLine(sc.r2s(axisX.getP1()), sc.r2s(axisX.getP2()));
        // ld.drawLine(sc.r2s(axisY.getP1()), sc.r2s(axisY.getP2()));
        drawAll(ld);

        /**/
        g.drawImage(bi, 0, 0, null);
    }

    private void drawAll(LineDrawer ld) {
        drawLine(ld, axisX);
        drawLine(ld, axisY);
        for (Line d : allLines)
            drawLine(ld, d);
        if (newLine != null) {
            drawLine(ld, newLine);
        }


    }

    private void drawLine(LineDrawer ld, Line l) {
        ld.drawLine(sc.r2s(l.getP1()), sc.r2s(l.getP2()));

    }

    private ScreenPoint prevPoint = null;

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    private Line newLine = null;

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
            newLine = null;
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