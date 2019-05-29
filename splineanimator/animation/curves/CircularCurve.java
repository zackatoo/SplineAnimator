package com.zackatoo.splineanimator.animation.curves;

import com.zackatoo.splineanimator.animation.Point;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;

public class CircularCurve extends Curve
{
    public CircularCurve(Point[] points, int color)
    {
        constructor(points, color);
    }

    public CircularCurve(Point center, int radius, int color)
    {
        double inverseRoot2 = 1 / Math.sqrt(2);
        Point[] points = new Point[8];
        points[0] = new Point(center.x, center.y - radius, center.thickness);
        points[1] = new Point(center.x + radius * inverseRoot2, center.y - radius * inverseRoot2, center.thickness);
        points[2] = new Point(center.x + radius, center.y, center.thickness);
        points[3] = new Point(center.x + radius * inverseRoot2, center.y + radius * inverseRoot2, center.thickness);
        points[4] = new Point(center.x, center.y + radius, center.thickness);
        points[5] = new Point(center.x - radius * inverseRoot2, center.y + radius * inverseRoot2, center.thickness);
        points[6] = new Point(center.x - radius, center.y, center.thickness);
        points[7] = new Point(center.x - radius * inverseRoot2, center.y - radius * inverseRoot2, center.thickness);

        constructor(points, color);
    }

    private void constructor(Point[] points, int color)
    {
        if (points == null) throw new NullPointerException();
        if (points.length <= 2) throw new ArrayIndexOutOfBoundsException("Array must contain at least three points for a circular curve");

        double[] xPoints = new double[points.length + 4];
        double[] yPoints = new double[points.length + 4];
        double[] thickPoints = new double[points.length + 4];
        double[] stepPoints = new double[points.length + 4];

        for (int i = 2; i < points.length; i++)
        {
            xPoints[i] = points[i].x;
            yPoints[i] = points[i].y;
            thickPoints[i] = points[i].thickness;
            stepPoints[i] = i;
        }

        xPoints[0] = points[points.length - 2].x;
        xPoints[1] = points[points.length - 1].x;
        xPoints[xPoints.length - 2] = points[0].x;
        xPoints[xPoints.length - 1] = points[1].x;

        yPoints[0] = points[points.length - 2].y;
        yPoints[1] = points[points.length - 1].y;
        yPoints[xPoints.length - 2] = points[0].y;
        yPoints[xPoints.length - 1] = points[1].y;

        thickPoints[0] = points[points.length - 2].thickness;
        thickPoints[1] = points[points.length - 1].thickness;
        thickPoints[thickPoints.length - 2] = points[0].thickness;
        thickPoints[thickPoints.length - 1] = points[1].thickness;

        SplineInterpolator interpolator = new SplineInterpolator();
        xFunction = interpolator.interpolate(stepPoints, xPoints);
        yFunction = interpolator.interpolate(stepPoints, yPoints);
        thickFunction = interpolator.interpolate(stepPoints, thickPoints);
        this.color = color;
        endStep = points.length * 2;
        startStep = points.length;
    }
}
