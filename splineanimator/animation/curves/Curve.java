package com.zackatoo.splineanimator.animation.curves;

import com.zackatoo.splineanimator.animation.ImageUtils;
import com.zackatoo.splineanimator.animation.Point;
import marvin.image.MarvinImage;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Curve
{
    protected ArrayList<Point> points;
    protected PolynomialSplineFunction xFunction;
    protected PolynomialSplineFunction yFunction;
    protected PolynomialSplineFunction thickFunction;
    protected double startStep;
    protected double endStep;
    protected int stepOffset;
    protected int color;
    protected boolean modifiedSinceLastDraw = true;
    protected boolean linear;

    public Curve(Point[] points, int color)
    {
        this(new ArrayList<>(Arrays.asList(points)), color);
    }

    public Curve(List<Point> pointList, int color)
    {
        this(new ArrayList<>() {{addAll(pointList);}}, color);
    }

    public Curve(ArrayList<Point> points, int color)
    {
        if (points == null) throw new NullPointerException();
        if (points.size() <= 1) throw new ArrayIndexOutOfBoundsException("Array must contain at least two points for a curve");

        this.color = color;
        startStep = 0;
        endStep = points.size() - 1;
        this.points = points;
        linear = points.size() == 2;
    }

    protected Curve()
    {
    }

    public void draw(MarvinImage image, double step)
    {
        if (linear)
        {
            ImageUtils.drawLine(image, points.get(0).x, points.get(0).y, points.get(1).x, points.get(1).y, color);
            return;
        }
        if (modifiedSinceLastDraw)
        {
            updateSplines();
        }

        int _x = 0;
        int _y = 0;
        int pastX = (int) xFunction.value(startStep + stepOffset);
        int pastY = (int) yFunction.value(startStep + stepOffset);

        for (double i = step + startStep; i < endStep; i += step)
        {
            try
            {
                _x = (int) xFunction.value(i + stepOffset);
                _y = (int) yFunction.value(i + stepOffset);
            }
            catch (OutOfRangeException e)
            {
                e.printStackTrace();
            }
            // TODO: instead of drawing linear functions, maybe we should manually move over and up (possibly faster?)
            ImageUtils.drawLine(image, pastX, pastY, _x, _y, color);

            pastX = _x;
            pastY = _y;
        }

        // Draws the last segment manually since the spline function is funky with domain
        ImageUtils.drawLine(image, pastX, pastY, (int) xFunction.value(endStep), (int) yFunction.value(endStep), color);
    }

    public void updateSplines()
    {
        if (linear) return;

        double[] xPoints = new double[points.size()];
        double[] yPoints = new double[points.size()];
        double[] thickPoints = new double[points.size()];
        double[] stepPoints = new double[points.size()];

        for (int i = 0; i < points.size(); i++)
        {
            xPoints[i] = points.get(i).x;
            yPoints[i] = points.get(i).y;
            thickPoints[i] = points.get(i).thickness;
            stepPoints[i] = i + stepOffset;
        }

        SplineInterpolator interpolator = new SplineInterpolator();
        xFunction = interpolator.interpolate(stepPoints, xPoints);
        yFunction = interpolator.interpolate(stepPoints, yPoints);
        thickFunction = interpolator.interpolate(stepPoints, thickPoints);
        modifiedSinceLastDraw = false;
    }

    public int getStepOffset()
    {
        return stepOffset;
    }

    public void setStepOffset(int stepOffset)
    {
        this.stepOffset = stepOffset;
    }

    public ArrayList<Point> getPoints()
    {
        return points;
    }

    public void setPoint(int index, Point point)
    {
        points.set(index, point);
    }
}
