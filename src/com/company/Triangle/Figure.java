package com.company.Triangle;

import com.company.point.RealPoint;

import java.util.ArrayList;
import java.util.List;

public class Figure  {

    private List<RealPoint> list = new ArrayList<>();

    public Figure() {
    }

    public Figure(List<RealPoint> list){
        this.list = list;
    }

    public List<RealPoint> getList() {
        return list;
    }

    public void addPoint(RealPoint p) {
            list.add(p);
    }

    public int getSize(){
        return list.size();
    }
}
