package com.company;

import java.awt.*;
import java.util.ArrayList;

public class DDALineDrawer implements LineDrawer, TriangleDrawer {
    private PixelDrawer pd;

    public DDALineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawLine(ScreenPoint p1, ScreenPoint p2) {
        int x1 = p1.getX(), y1 = p1.getY();
        int x2 = p2.getX(), y2 = p2.getY();
        double dx = x2 - x1;
        double dy = y2 - y1;

        if (Math.abs(dy) > Math.abs(dx)) {
            double reversek = dx / dy;

            if (y1 > y2) {
                int tmp = y2;
                y2 = y1;
                y1 = tmp;
                tmp = x2;
                x2 = x1;
                x1 = tmp;
            }
            for (int i = y1; i < y2; i++) {
                double j = (i - y1) * reversek + x1;
                pd.colorPixel((int) j, i, Color.RED);
            }
        } else {

            double k = dy / dx;
            if (x1 > x2) {
                int tmp = y2;
                y2 = y1;
                y1 = tmp;
                tmp = x2;
                x2 = x1;
                x1 = tmp;
            }
            for (int j = x1; j <= x2; j++) {
                double i = (j - x1) * k + y1;
                pd.colorPixel(j, (int) i, Color.BLUE);

            }
        }

    }


    @Override
    public void drawTriangle(ScreenPoint p1, ScreenPoint p2) {
        drawLine(p1, p2);
        drawLine(p2, new ScreenPoint((p2.getX() + p1.getX()) / 2, (int)(Math.sqrt(2)/3)*(p2.getY() - p1.getY()) ));
        drawLine(new ScreenPoint((p2.getX() + p1.getX()) / 2, (int)(Math.sqrt(2)/3)*(p2.getX() - p1.getY()) ), p1);
    }
}

