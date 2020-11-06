package com.company.Triangle;

import com.company.point.RealPoint;

import java.util.*;

public class Triangle {
    private List<RealPoint> list = new ArrayList<>();
    private boolean complete = false;

    public Triangle(List<RealPoint> list) {
        this.list = list;
            complete = true;
    }

    public Triangle() {
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isComplete() {
        return complete;
    }

    public List<RealPoint> getList() {
        return list;
    }

    public void addPoint(RealPoint p) {
        list.add(p);
    }

    public Triangle getSortTriangle(Triangle t) {
        ArrayList<RealPoint> points = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            points.add(t.getList().get(i));
        }
        points.sort(new Comparator<RealPoint>() {

            public int compare(RealPoint o1, RealPoint o2) {
                return Integer.compare((int) o1.getX(), (int) o2.getX());
            }
        });
        return new Triangle(points);
    }
}

