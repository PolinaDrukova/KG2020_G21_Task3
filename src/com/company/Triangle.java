package com.company;

import java.util.*;

public class Triangle {
    private RealPoint p1, p2, p3;

    public Triangle(RealPoint p1, RealPoint p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = new RealPoint((p2.getX() + p1.getX()) / 2, (Math.sqrt(3) / 2) * (p2.getY() - p1.getY()));


    }

    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {

        this.p1 = new RealPoint(x1, y1);
        this.p2 = new RealPoint(x2, y2);
        this.p3 = new RealPoint((x2 + x1) / 2, (Math.sqrt(3) / 2) * (y2 - y1));
    }

    public RealPoint getP1() {
        return p1;
    }

    public RealPoint getP2() {
        return p2;
    }

    public RealPoint getP3() {
        return p3;
    }


    public List<RealPoint> getListOfPoints(Triangle t) {
        ArrayList<RealPoint> points = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            points.add(t.p1);
            points.add(t.p2);
            points.add(t.p3);
        }
        points.sort(new Comparator<RealPoint>() {

            public int compare(RealPoint o1, RealPoint o2) {
                return Integer.compare((int) o1.getY(), (int) o2.getY());
            }
        });
        return points;
    }
}

