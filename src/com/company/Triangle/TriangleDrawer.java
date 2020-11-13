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


    private static RealPoint getCrossingPoint(List<RealPoint> points) {//точка пересечения двух сторон
        double a1 = points.get(0).getY() - points.get(1).getY();
        double b1 = points.get(1).getX() - points.get(0).getX();
        double a2 = points.get(2).getY() - points.get(3).getY();
        double b2 = points.get(3).getX() - points.get(2).getX();

        double d = a1 * b2 - a2 * b1;
        if (d != 0) {
            double c1 = points.get(1).getY() * points.get(0).getX() - points.get(1).getX() * points.get(0).getY();
            double c2 = points.get(3).getY() * points.get(2).getX() - points.get(3).getX() * points.get(2).getY();

            double xi = (int) (b1 * c2 - b2 * c1) / d;
            double yi = (int) (a2 * c1 - a1 * c2) / d;
            return new RealPoint(xi, yi);
        }
        return null;
    }


   /*виды взаимодействия треугольников:
    1) один в другом
    если все точки 1 треугольника принадлежат 2 ,то удалить точки 1 и наоборот
    2) не соприкасаются
    если хотя бы одна точка 1 не принадлежит 2, то ничего не делать и наоборот ))
    3)пересекаются
    если хотя бы одна точка 1 треугольника принадлежит 2(то бишь лежит внутри него)
    удалить точку и найти 2 новые точки пересечения
     и наоборот

     не забывать сортировать точки
    */

    public static List<RealPoint> pointsOfNewPolygon(Triangle t1, Triangle t2) {
        List<RealPoint> points1 = new ArrayList<>();
        List<RealPoint> p = new ArrayList<>();
        Triangle st1 = t1.getSortTriangle(t1);
       Triangle st2 = t2.getSortTriangle(t2);

        for (int i = 0; i < 3; i++) {
            points1.add(t1.getList().get(i));
        }
        for (int i = 0; i < 3; i++) {
            points1.add(t2.getList().get(i));
        }
       getSortPoint(points1);

        if (isBelongs(st1, st2.getList().get(0)) && isBelongs(st1, st2.getList().get(1)) && isBelongs(st1, st2.getList().get(2))) {
            for (int i = 0; i < points1.size(); i++) {
                for (int j = 0; j < 3; j++) {
                    if (points1.get(i) == st2.getList().get(j)) {
                        points1.remove(points1.get(i));
                    }
                }
            }
        }

        if (isBelongs(st2, st1.getList().get(0)) && isBelongs(st2, st1.getList().get(1)) && isBelongs(st2, st1.getList().get(2))) {
            for (int i = 0; i < points1.size(); i++) {
                for (int j = 0; j < 3; j++) {
                    if (points1.get(i) == st1.getList().get(j)) {
                        points1.remove(points1.get(i));
                    }
                }
            }
        }


        if ((points1.size() == 6)) {
            for (int i = 0; i < 3; i++) {
                if (isBelongs(st1, st2.getList().get(i))) {
                    for (int j = 0; j < points1.size(); j++) {
                        if (points1.get(j) == st2.getList().get(i)) {
                            p.add(points1.get(j));
                            points1.remove(points1.get(j));
                        }
                    }
                }
            }
        }

        return getSortPoint(points1);

    }


    public static List<RealPoint> getSortPoint(List<RealPoint> point) {
        ArrayList<RealPoint> points = new ArrayList<>(point);
        points.sort(Comparator.comparingInt(o -> (int) o.getY()));
        return points;
    }

    public static boolean noChange1(Triangle t1, Triangle t2) {
        if (!(isBelongs(t1, t2.getList().get(0)) && isBelongs(t1, t2.getList().get(1)) && isBelongs(t1, t2.getList().get(2)))) {
            return true;
        }
        return false;
    }

    public static boolean noChange2(Triangle t1, Triangle t2) {
        if (!(isBelongs(t2, t1.getList().get(0)) && isBelongs(t2, t1.getList().get(1)) && isBelongs(t2, t1.getList().get(2)))) {
            return true;
        }
        return false;
    }


}
