package com.company;

import com.company.Triangle.Figure;
import com.company.Triangle.Triangle;
import com.company.Triangle.TriangleDrawer;
import com.company.line_drawer.BrezenhamCircleDrawer;
import com.company.line_drawer.CircleDrawer;
import com.company.line_drawer.DDALineDrawer;
import com.company.line_drawer.LineDrawer;
import com.company.pixel_drawer.BufferedImagePixelDrawer;
import com.company.pixel_drawer.PixelDrawer;
import com.company.point.RealPoint;
import com.company.point.ScreenPoint;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    public DrawPanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    private ScreenConverter sc = new ScreenConverter(-2, 2, 4, 4, 800, 600);

    private Line axisX = new Line(-1.5, -1.5, 1.5, -1.5);
    private Line axisY = new Line(-1.5, -1.5, -1.5, 1.5);

    private Line newLine = null;
    private ScreenPoint prevPoint = null;

    private int x = 0, y = 0;
    private int x0 = 0, y0 = 0;
    private int radius = 20;

    private List<Triangle> triangles = new ArrayList<>();
    private Figure figure = new Figure();
    private RealPoint changePoint;
    private boolean complete = true;

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
        CircleDrawer cd = new BrezenhamCircleDrawer(pd);
        /**/
        drawAll(ld, cd);
        /**/
        g.drawImage(bi, 0, 0, null);
        gr.dispose();
    }

    private void drawAll(LineDrawer ld, CircleDrawer cd) {

        drawLine(ld, axisX);
        drawLine(ld, axisY);

        drawTriangles(ld, cd);
        drawClosingSide(ld, cd);
        setChangeMarker();

        drawFigure(ld);

    }


    private void drawLine(LineDrawer ld, Line l) {
        ld.drawLine(sc.r2s(l.getP1()), sc.r2s(l.getP2()), Color.BLUE);
    }

    private void drawTriangles(LineDrawer ld, CircleDrawer cd) {
        int lines = 0;
        int isComplete;
        for (Triangle t : triangles) {
            if (complete) {
                isComplete = 0;
            } else {
                isComplete = 1;
            }
            if (lines != triangles.size() - isComplete) {
                TriangleDrawer.drawFinal(sc, ld, t, cd);
                lines++;
            }

        }
    }

    private void drawClosingSide(LineDrawer ld, CircleDrawer cd) {
        if (triangles.size() > 0 && !complete) {
            Triangle t = triangles.get(triangles.size() - 1);
            TriangleDrawer.draw(sc, ld, t, cd);
            List<RealPoint> points = t.getList();
            if (points.size() > 0) {
                RealPoint p = points.get(points.size() - 1);
                ScreenPoint sp = sc.r2s(p);
                ScreenPoint sp2 = new ScreenPoint(x, y);
                ld.drawLine(sp, sp2, Color.BLUE);
            }
        }
    }

    private void drawFigure(LineDrawer ld) {
        List<RealPoint> points = figure.getList();
        for (int i = 0; i < points.size() - 1; i++) {
            ld.drawLine(sc.r2s(points.get(i)), sc.r2s(points.get(i + 1)), Color.RED);
            ld.drawLine(sc.r2s(points.get(0)), sc.r2s(points.get(points.size() - 1)), Color.RED);
        }
    }


    public void setChangeMarker() {
        if (changePoint != null) {
            RealPoint p = sc.s2r(new ScreenPoint(x, y));
            changePoint.setX(p.getX());
            changePoint.setY(p.getY());
        }
    }

    public RealPoint nearMarker(int from, int to) {
        for (Triangle t : triangles) {
            for (RealPoint realPoint : t.getList()) {
                ScreenPoint sp = sc.r2s(realPoint);
                if (Math.abs(from - sp.getX()) < radius && Math.abs(to - sp.getY()) < radius) {
                    return realPoint;
                }
            }
        }
        return null;
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            x = mouseEvent.getX();
            y = mouseEvent.getY();
            if (changePoint != null) {
                if (!(Math.abs(x - changePoint.getX()) < radius && Math.abs(y - changePoint.getY()) < radius)) {
                    RealPoint p = sc.s2r(new ScreenPoint(x, y));
                    changePoint.setX(p.getX());
                    changePoint.setY(p.getY());
                }
                changePoint = null;
            } else if (complete) {
                changePoint = nearMarker(x, y);
                if (changePoint == null) {
                    triangles.add(new Triangle());
                    x0 = x;
                    y0 = y;
                    RealPoint p = sc.s2r(new ScreenPoint(x, y));
                    triangles.get(triangles.size() - 1).addPoint(p);
                    complete = false;
                }
            } else {
                if (Math.abs(x - x0) < radius && Math.abs(y - y0) < radius) {
                    complete = true;
                } else {
                    RealPoint p = sc.s2r(new ScreenPoint(x, y));
                    triangles.get(triangles.size() - 1).addPoint(p);
                }

            }
            repaint();
            if ((triangles.size() == 2) && (triangles.get(0).getList().size() == 3) && (triangles.get(1).getList().size() == 3)) {
                figure = new Figure(TriangleDrawer.pointsOfNewPolygon(triangles.get(0), triangles.get(1)));
            }
            repaint();
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (!complete) {
                complete = true;
            }
            prevPoint = new ScreenPoint(e.getX(), e.getY());
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

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
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        if (!complete || changePoint != null) {
            repaint();
        }

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