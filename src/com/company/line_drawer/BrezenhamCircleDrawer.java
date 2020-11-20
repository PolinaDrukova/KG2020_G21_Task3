package com.company.line_drawer;

import com.company.pixel_drawer.PixelDrawer;

import java.awt.*;

public class BrezenhamCircleDrawer implements CircleDrawer{
    private PixelDrawer pd;

    public BrezenhamCircleDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawCircle(int x, int y, int r) {
        int x1 = r;
        int y1 = 0;
        int d = 1 - x1;
        while (x1 >= y1) {
            pd.colorPixel(x1 + x + r, y1 + y + r, Color.BLACK);
            pd.colorPixel(y1 + x + r, x1 + y + r, Color.BLACK);
            pd.colorPixel(-x1 + x + r, y1 + y + r, Color.BLACK);
            pd.colorPixel(-y1 + x + r, x1 + y + r, Color.BLACK);
            pd.colorPixel(-x1 + x + r, -y1 + y + r, Color.BLACK);
            pd.colorPixel(-y1 + x + r, -x1 + y + r, Color.BLACK);
            pd.colorPixel(x1 + x + r, -y1 + y + r, Color.BLACK);
            pd.colorPixel(y1 + x + r, -x1 + y + r, Color.BLACK);
            y1++;
            if (d < 0) {
                d += 2 * y1 + 1;
            } else {
                x1--;
                d += 2 * (y1 - x1 + 1);
            }
        }
    }
}
