package com.company.lineDrawer;

import com.company.point.ScreenPoint;

import java.awt.*;

public interface LineDrawer {
    void drawLine(ScreenPoint p1, ScreenPoint p2, Color color);
}
