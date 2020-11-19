package com.company.Triangle;

import com.company.Line;
import com.company.line_drawer.CircleDrawer;
import com.company.line_drawer.LineDrawer;
import com.company.point.RealPoint;
import com.company.ScreenConverter;
import com.company.point.ScreenPoint;


import java.awt.*;
import java.util.*;
import java.util.List;


public class TriangleDrawer {

    public static void draw(ScreenConverter sc, LineDrawer ld, Triangle t, CircleDrawer cd) {//две стороны треугольника, complete = false
        RealPoint prev = null;
        for (RealPoint p : t.getList()) {
            if (prev != null) {
                ScreenPoint p1 = sc.r2s(prev);
                ScreenPoint p2 = sc.r2s(p);
                ld.drawLine(p1, p2, Color.BLUE);
                cd.drawCircle(p1.getX() - 5, p1.getY() - 5, 5);
            }
            prev = p;

        }
    }

    public static void drawFinal(ScreenConverter sc, LineDrawer ld, Triangle t, CircleDrawer cd) {//завершенный треугольник, complete = true
        draw(sc, ld, t, cd);
        ScreenPoint p1 = sc.r2s(t.getList().get(t.getList().size() - 1));
        ScreenPoint p2 = sc.r2s(t.getList().get(0));
        ld.drawLine(p1, p2, Color.BLUE);
        cd.drawCircle(p1.getX() - 5, p1.getY() - 5, 5);
    }

    public static void drawFigure(ScreenConverter sc, LineDrawer ld, Figure t) {//две стороны треугольника, complete = false
        RealPoint prev = null;
        for (RealPoint p : t.getList()) {
            if (prev != null) {
                ScreenPoint p1 = sc.r2s(prev);
                ScreenPoint p2 = sc.r2s(p);
                ld.drawLine(p1, p2, Color.RED);
            }
            prev = p;

        }
    }

    public static void drawFinalFigure(ScreenConverter sc, LineDrawer ld, Figure t) {//завершенный треугольник, complete = true
        drawFigure(sc, ld, t);
        ScreenPoint p1 = sc.r2s(t.getList().get(t.getList().size() - 1));
        ScreenPoint p2 = sc.r2s(t.getList().get(0));
        ld.drawLine(p1, p2, Color.RED);
    }





    private static boolean isBelongs(Triangle t, RealPoint p) {//принадлежит ли точка треугольнику
        double m, l = 0;
        double bx, by, cx, cy, px, py;
        boolean result = false;
        // переносим треугольник точкой А в (0;0).
        bx = t.getList().get(1).getX() - t.getList().get(0).getX();
        by = t.getList().get(1).getY() - t.getList().get(0).getY();
        cx = t.getList().get(2).getX() - t.getList().get(0).getX();
        cy = t.getList().get(2).getY() - t.getList().get(0).getY();
        px = p.getX() - t.getList().get(0).getX();
        py = p.getY() - t.getList().get(0).getY();

        m = (px * by - bx * py) / (cx * by - bx * cy);
        if ((m >= 0) && (m <= 1)) {
            l = (px - m * cx) / bx;
        }
        if ((l >= 0) && ((m + l) <= 1)) {
            result = true;
        }
        return result;
    }

    private static RealPoint getCrossingPoint(Line l1, Line l2) { //поиск точки пересечения 2 линий
        double x1 = l1.getP1().getX();
        double y1 = l1.getP1().getY();
        double x2 = l1.getP2().getX();
        double y2 = l1.getP2().getY();
        double x3 = l2.getP1().getX();
        double y3 = l2.getP1().getY();
        double x4 = l2.getP2().getX();
        double y4 = l2.getP2().getY();

        if ((x1 == x3) && (y1 == y3) || (x1 == x4) && (y1 == y4) || (x2 == x3) && (y2 == y3) || (x2 == x4) && (y2 == y4)) {
            return null;
        } else {

            double a1 = y2 - y1;
            double b1 = x1 - x2;
            double c1 = (-x1 * y2 + y1 * x2);

            double a2 = y4 - y3;
            double b2 = x3 - x4;
            double c2 = (-x3 * y4 + y3 * x4);

            double x = (b1 * c2 - b2 * c1) / (a1 * b2 - a2 * b1);
            double y = (a2 * c1 - a1 * c2) / (a1 * b2 - a2 * b1);

            if (((x1 < x) && (x2 > x) && (x3 < x) && (x4 > x)) || ((y1 < y) && (y2 > y) && (y3 < y) && (y4 > y))) {
                return new RealPoint(x, y);
            } else {
                return null;
            }
        }
    }

    private static boolean belongLine(Line l, RealPoint p1) {//принадлежит ли точка линии
        if ((l.getP1().getX() < p1.getX()) && (l.getP1().getY() < p1.getY())
                &&
                (l.getP2().getX() > p1.getX()) && (l.getP2().getY() > p1.getY())) {
            return true;
        }
        return false;

    }

    public static List<RealPoint> pointsOfNewPolygon(Triangle t1, Triangle t2) {
        List<RealPoint> finalPoints = new ArrayList<>();

        RealPoint a1 = t1.getList().get(0);
        RealPoint a2 = t1.getList().get(1);
        RealPoint a3 = t1.getList().get(2);
        RealPoint b1 = t2.getList().get(0);
        RealPoint b2 = t2.getList().get(1);
        RealPoint b3 = t2.getList().get(2);

        List<RealPoint> p = new ArrayList<>(Arrays.asList(a1, a2, a3, b1, b2, b3));

        Line l1t1 = new Line(a1, a2);
        Line l2t1 = new Line(a1, a3);
        Line l3t1 = new Line(a2, a3);
        List<Line> linesT1 = new ArrayList<>(Arrays.asList(l1t1, l2t1, l3t1));

        Line l1t2 = new Line(b1, b2);
        Line l2t2 = new Line(b1, b3);
        Line l3t2 = new Line(b2, b3);
        List<Line> linesT2 = new ArrayList<>(Arrays.asList(l1t2, l2t2, l3t2));

        if (isBelongs(t2, a1)) {
            if (isBelongs(t2, a2)) {
                if (isBelongs(t2, a3)) {
                    finalPoints.add(a1);
                    finalPoints.add(a2);
                    finalPoints.add(a3);
                }
            }
        }

        if (isBelongs(t1, b1)) {
            if (isBelongs(t1, b2)) {
                if (isBelongs(t1, b3)) {
                    finalPoints.add(b1);
                    finalPoints.add(b2);
                    finalPoints.add(b3);
                }
            }
        }

        for (int i = 0; i < linesT1.size(); i++) {
            for (int j = 0; j < linesT2.size(); j++) {
                if (((getCrossingPoint(linesT1.get(j), linesT2.get(i)) != null))) {
                    finalPoints.add(getCrossingPoint(linesT1.get(j), linesT2.get(i)));
                }

            }
        }


        for (int i = 0; i < p.size(); i++) {
            for (int j = i + 1; j <p.size() ; j++) {
                if(p.get(i) == p.get(j)){
                    finalPoints.add(p.get(i));
                }
            }
        }
        return sortPoints(finalPoints);

    }

    public static void sortPointsClockwise(List<RealPoint> points) {
        float averageX = 0;
        float averageY = 0;

        for (RealPoint point : points) {
            averageX += point.getX();
            averageY += point.getY();
        }

        final float finalAverageX = averageX / points.size();
        final float finalAverageY = averageY / points.size();

        Comparator<RealPoint> comparator = new Comparator<RealPoint>() {

            public int compare(RealPoint lhs, RealPoint rhs) {
                double lhsAngle = Math.atan2(lhs.getY() - finalAverageY, lhs.getX() - finalAverageX);
                double rhsAngle = Math.atan2(rhs.getY() - finalAverageY, rhs.getX() - finalAverageX);

                // Depending on the coordinate system, you might need to reverse these two conditions
                if (lhsAngle < rhsAngle) return -1;
                if (lhsAngle > rhsAngle) return 1;

                return 0;
            }
        };

        points.sort(comparator);
    }

    public static List<RealPoint> sortPoints(List<RealPoint> points) {
        sortPointsClockwise(points);
        Collections.reverse(points);
        return points;
    }
}