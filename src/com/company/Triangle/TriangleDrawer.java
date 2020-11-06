package com.company.Triangle;

import com.company.line_drawer.LineDrawer;
import com.company.point.RealPoint;
import com.company.ScreenConverter;
import com.company.point.ScreenPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class TriangleDrawer {

    public static void draw(ScreenConverter sc, LineDrawer ld, Triangle t) {//две стороны треугольника, complete = false
        RealPoint prev = null;
        for (RealPoint p : t.getList()) {
            if (prev != null) {
                ScreenPoint p1 = sc.r2s(prev);
                ScreenPoint p2 = sc.r2s(p);
                ld.drawLine(p1, p2);
            }
            prev = p;
        }
    }

    public static void drawFinal(ScreenConverter sc, LineDrawer ld, Triangle t) {//завершенный треугольник, complete = true
         draw(sc, ld, t);
        ScreenPoint p1 = sc.r2s(t.getList().get(t.getList().size() - 1));
        ScreenPoint p2 = sc.r2s(t.getList().get(0));
        ld.drawLine(p1, p2);

    }


    private static boolean isBelongs(Triangle t, RealPoint p) {
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

    public static List<RealPoint> pointsOfNewPolygon(Triangle t1, Triangle t2) {
        List<RealPoint> points1 = new ArrayList<>();
        List<RealPoint> finalPoints = new ArrayList<>();
        Triangle st1 = t1.getSortTriangle(t1);
        Triangle st2 = t2.getSortTriangle(t2);

        points1.add(st1.getList().get(0));
        points1.add(st1.getList().get(1));
        points1.add(st1.getList().get(2));
        points1.add(st2.getList().get(0));
        points1.add(st1.getList().get(1));
        points1.add(st1.getList().get(2));

        List<RealPoint> points2 = getSortPoint(points1);

        for (int i = 0; i < points2.size(); i++) {
            if ((isBelongs(st1, points2.get(i)) || !isBelongs(st2, points2.get(i))) ||
                    (!isBelongs(st1, points2.get(i)) || isBelongs(st2, points2.get(i)))) {
                finalPoints.add(points2.get(i));

            }
        }
        return finalPoints;

    }


    public static List<RealPoint> getSortPoint(List<RealPoint> point) {
        ArrayList<RealPoint> points = new ArrayList<>(point);
        points.sort(new Comparator<RealPoint>() {
            public int compare(RealPoint o1, RealPoint o2) {
                return Integer.compare((int) o1.getY(), (int) o2.getY());
            }
        });
        return points;
    }

}
