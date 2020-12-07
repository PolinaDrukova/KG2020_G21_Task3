package com.company.triangle;

import com.company.point.RealPoint;

public class Triangle extends Figure{

    public Triangle(RealPoint p1, RealPoint p2, RealPoint p3) {
        this.addPoint(p1);
        this.addPoint(p2);
        this.addPoint(p3);
    }

    public Triangle() {
    }

    @Override
    public void addPoint(RealPoint p) {
        if(this.getSize() < 3) {
            super.addPoint(p);
        }
    }
}
