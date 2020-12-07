package com.company.triangle;

import com.company.Line;
import com.company.lineDrawer.LineDrawer;
import com.company.point.RealPoint;
import com.company.ScreenConverter;
import com.company.point.ScreenPoint;


import java.awt.*;
import java.util.*;
import java.util.List;


public class TriangleDrawer {

    public static void drawTriangle(ScreenConverter sc, LineDrawer ld, Triangle t) {//две стороны треугольника, complete = false
        RealPoint prev = null;
        for (RealPoint point : t.getList()) {
            if (prev != null) {
                ScreenPoint p1 = sc.r2s(prev);
                ScreenPoint p2 = sc.r2s(point);
                ld.drawLine(p1, p2, Color.BLUE);

            }
            prev = point;

        }
    }

    public static void drawFinalSide(ScreenConverter sc, LineDrawer ld, Triangle t) {//завершенный треугольник, complete = true
        drawTriangle(sc, ld, t);
        ScreenPoint p1 = sc.r2s(t.getList().get(t.getList().size() - 1));
        ScreenPoint p2 = sc.r2s(t.getList().get(0));
        ld.drawLine(p1, p2, Color.BLUE);

    }

    private static boolean isBelongs(Triangle t, RealPoint p) {//принадлежит ли точка треугольнику
        double x1 = t.getList().get(0).getX();
        double y1 = t.getList().get(0).getY();
        double x2 = t.getList().get(1).getX();
        double y2 = t.getList().get(1).getY();
        double x3 = t.getList().get(2).getX();
        double y3 = t.getList().get(2).getY();

        double x0 = p.getX();
        double y0 = p.getY();

        double a = (x1 - x0) * (y2 - y1) - (x2 - x1) * (y1 - y0);
        double b = (x2 - x0) * (y3 - y2) - (x3 - x2) * (y2 - y0);
        double c = (x3 - x0) * (y1 - y3) - (x1 - x3) * (y3 - y0);

        if ((a >= 0 && b >= 0 && c >= 0) || (a <= 0 && b <= 0 && c <= 0)) {//если одинакового знака лежит внутри, если 0 - лежит на стороне
            return true;
        } else {
            return false;
        }

    }

    private static RealPoint getCrossingPoint(Line l1, Line l2) {//поиск точки пересечения двух линий
        double x1 = l1.getP1().getX();
        double y1 = l1.getP1().getY();
        double x2 = l1.getP2().getX();
        double y2 = l1.getP2().getY();
        double x3 = l2.getP1().getX();
        double y3 = l2.getP1().getY();
        double x4 = l2.getP2().getX();
        double y4 = l2.getP2().getY();
        double v1, v2, v3, v4;
        double xv12, xv13, xv14, xv31, xv32, xv34, yv12, yv13, yv14, yv31, yv32, yv34;

        xv12 = x2 - x1; //координаты векторов
        xv13 = x3 - x1;
        xv14 = x4 - x1;
        yv12 = y2 - y1;
        yv13 = y3 - y1;
        yv14 = y4 - y1;

        xv31 = x1 - x3;
        xv32 = x2 - x3;
        xv34 = x4 - x3;
        yv31 = y1 - y3;
        yv32 = y2 - y3;
        yv34 = y4 - y3;

        v1 = xv34 * yv31 - yv34 * xv31;// векторные произведения
        v2 = xv34 * yv32 - yv34 * xv32;
        v3 = xv12 * yv13 - yv12 * xv13;
        v4 = xv12 * yv14 - yv12 * xv14;

        if ((v1 * v2) < 0 && (v3 * v4) < 0) {
            double A1, B1, C1, A2, B2, C2;
            A1 = y2 - y1;
            A2 = y4 - y3;
            B1 = x1 - x2;
            B2 = x3 - x4;
            C1 = (x1 * (y1 - y2) + y1 * (x2 - x1)) * (-1);
            C2 = (x3 * (y3 - y4) + y3 * (x4 - x3)) * (-1);


            double D = ((A1 * B2) - (B1 * A2));//детерминант
            double Dx = ((C1 * B2) - (B1 * C2));
            double Dy = ((A1 * C2) - (C1 * A2));

            if (D != 0) {//поиск неизвестных методом крамера, если детерминант не равен 0
                double x = (Dx / D);
                double y = (Dy / D);
                return new RealPoint(x, y);
            } else {
                return null;
            }
        }
        return null;
    }


    public static List<RealPoint> pointsOfNewPolygon(Triangle t1, Triangle t2) {//финальная функция
        List<RealPoint> finalPoints = new ArrayList<>();

        RealPoint a1 = t1.getList().get(0);
        RealPoint a2 = t1.getList().get(1);
        RealPoint a3 = t1.getList().get(2);
        RealPoint b1 = t2.getList().get(0);
        RealPoint b2 = t2.getList().get(1);
        RealPoint b3 = t2.getList().get(2);

        List<RealPoint> p1 = new ArrayList<>(Arrays.asList(a1, a2, a3));
        List<RealPoint> p2 = new ArrayList<>(Arrays.asList(b1, b2, b3));

        Line l1t1 = new Line(a1, a2);
        Line l2t1 = new Line(a1, a3);
        Line l3t1 = new Line(a2, a3);
        List<Line> linesT1 = new ArrayList<>(Arrays.asList(l1t1, l2t1, l3t1));

        Line l1t2 = new Line(b1, b2);
        Line l2t2 = new Line(b1, b3);
        Line l3t2 = new Line(b2, b3);
        List<Line> linesT2 = new ArrayList<>(Arrays.asList(l1t2, l2t2, l3t2));

        if (isBelongs(t2, a1)) {
            finalPoints.add(a1);
        }
        if (isBelongs(t2, a2)) {
            finalPoints.add(a2);
        }
        if (isBelongs(t2, a3)) {
            finalPoints.add(a3);
        }

        if (isBelongs(t1, b1)) {
            finalPoints.add(b1);
        }
        if (isBelongs(t1, b2)) {
            finalPoints.add(b2);
        }
        if (isBelongs(t1, b3)) {
            finalPoints.add(b3);
        }


        if (p1.size() == 3 && p2.size() == 3) {
            for (int i = 0; i < linesT1.size(); i++) {
                for (int j = 0; j < linesT2.size(); j++) {
                    RealPoint crossingPoint = getCrossingPoint(linesT1.get(j), linesT2.get(i));
                    if (crossingPoint != null) {
                        finalPoints.add(crossingPoint);
                    }

                }
            }

            for (RealPoint realPoint : p1) {
                for (RealPoint point : p2) {
                    if (realPoint == point) {
                        finalPoints.add(realPoint);
                    }
                }
            }
        }

        return sortPoints(finalPoints);
    }

    //определяет угол по вектору из усредненной точки на каждую вершину и сортирует по углу
    private static List<RealPoint> sortPoints(List<RealPoint> points) {

        float averageX = 0;
        float averageY = 0;

        for (RealPoint point : points) {
            averageX += point.getX();
            averageY += point.getY();
        }

        float finalAverageX = averageX / points.size();
        float finalAverageY = averageY / points.size();

        Comparator<RealPoint> comparator = new Comparator<RealPoint>() {

            public int compare(RealPoint p1, RealPoint p2) {
                double angle1 = Math.atan2(p1.getY() - finalAverageY, p1.getX() - finalAverageX);
                double angle2 = Math.atan2(p2.getY() - finalAverageY, p2.getX() - finalAverageX);

                if (angle1 < angle2) return -1;
                if (angle1 > angle2) return 1;

                return 0;
            }
        };

        points.sort(comparator);
        return points;
    }

}