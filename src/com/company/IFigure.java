package com.company;

import com.company.point.RealPoint;

public interface IFigure {
    void setChangeMarker();
    RealPoint nearMarker(int from, int to);
}
