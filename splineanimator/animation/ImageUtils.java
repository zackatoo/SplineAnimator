package com.zackatoo.splineanimator.animation;

import marvin.image.MarvinImage;

public class ImageUtils
{
    // Draws filled circles
    public static void drawCircle(MarvinImage image, int x0, int y0, int radius, int color)
    {
        int x = radius - 1;
        int y = 0;
        int dx = 1;
        int dy = 1;
        int err = dx - (radius << 1);

        while(x >= y)
        {
            drawHorizontalLine(image, x0 + x, x0 - x, y0 + y, color);
            drawHorizontalLine(image, x0 + y, x0 - y, y0 + x, color);
            drawHorizontalLine(image, x0 + x, x0 - x, y0 - y, color);
            drawHorizontalLine(image, x0 + y, x0 - y, y0 - x, color);

            if (err <= 0)
            {
                y++;
                err += dy;
                dy += 2;
            }

            if (err > 0)
            {
                x--;
                dx += 2;
                err += dx - (radius << 1);
            }
        }
    }

    // Draws horizontal line
    public static void drawHorizontalLine(MarvinImage image, int x1, int x2, int y, int color)
    {
        for (; x2 < x1; x2++)
        {
            if (x2 < image.getWidth() && x2 >= 0 && y < image.getHeight() && y >= 0)
            {
                image.setIntColor(x2, y, color, color, color);
            }
        }
    }

    // Draws a single pixel linear line
    public static void drawLine(MarvinImage image, int x1, int y1, int x2, int y2, int color)
    {
        double slope = ((double)y2 - y1) / (x2 - x1);

        if (Math.abs(slope) <= 1)
        {
            double yIntercept = -x1 * slope + y1;
            drawXbasedLine(image, min(x1, x2), max(x1, x2), slope, yIntercept, color);
        }
        else
        {
            double xIntercept = -y1 / slope + x1;
            drawYbasedLine(image, min(y1, y2), max(y1, y2), slope, xIntercept, color);
        }
    }

    private static void drawXbasedLine(MarvinImage image, int startX, int endX, double slope, double yIntercept, int color)
    {
        for (int x = startX; x < endX; x++)
        {
            if (x < image.getWidth() && x >= 0)
            {
                int y = (int) Math.round(x * slope + yIntercept);
                if (y < image.getHeight() && y >= 0)
                {
                    image.setIntColor(x, y, color);
                }
            }
        }
    }

    private static void drawYbasedLine(MarvinImage image, int startY, int endY, double slope, double xIntercept, int color)
    {
        for (int y = startY; y < endY; y++)
        {
            if (y < image.getHeight() && y >= 0)
            {
                int x = (int) Math.round(y / slope + xIntercept);
                if (x < image.getWidth() && x >= 0)
                {
                    image.setIntColor(x, y, color);
                }
            }
        }
    }

    public static int rgb2integer(int r, int g, int b)
    {
        return (r << 16) + (g << 8) + b;
    }

    private static int min(int one, int two)
    {
        return (one < two) ? one : two;
    }

    private static int max(int one, int two)
    {
        return (one > two) ? one : two;
    }
}
