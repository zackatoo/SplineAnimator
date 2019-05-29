package com.zackatoo.splineanimator.animation.curves;

import com.zackatoo.splineanimator.animation.Point;
import marvin.image.MarvinImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PiecewiseCurve
{
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Curve> curves = new ArrayList<>();
    private int color = 0xFF000000;
    private boolean modifiedSinceLastDraw = true;

    public PiecewiseCurve(String fileName)
            throws Exception
    {
        Scanner reader = new Scanner(new File(fileName));
        StringBuilder sb = new StringBuilder();
        String line = reader.nextLine();
        int lineNum = 0;
        if (!line.contains(","))
        {
            try
            {
                color = Integer.parseInt(line);
                line = reader.nextLine();
                lineNum++;
            }
            catch (NumberFormatException e)
            {
                sb.append("Line 1: ");
                sb.append(e.getMessage());
                sb.append("\n");
            }
        }

        while (reader.hasNextLine())
        {
            lineNum++;
            if (line.equals(""))
            {
                break;
            }
            else
            {
                try
                {
                    points.add(new Point(line, lineNum));
                }
                catch (Exception e)
                {
                    sb.append(e.getMessage());
                    sb.append("\n");
                }
            }
            line = reader.nextLine();
        }

        if (sb.length() != 0)
        {
            sb.deleteCharAt(sb.length() - 1);
            throw new Exception(sb.toString());
        }
    }

    public PiecewiseCurve(Point[] points, int color)
    {
        this(new ArrayList<>(Arrays.asList(points)), color);
    }

    public PiecewiseCurve(ArrayList<Point> points, int color)
    {
        this.points = points;
        this.color = color;
    }

    public PiecewiseCurve(int color)
    {
        this.color = color;
    }

    public PiecewiseCurve()
    {
    }

    public void draw(MarvinImage image, double step)
    {
        if (points.size() < 2) throw new ArrayIndexOutOfBoundsException("Must have at least two points to draw");

        if (modifiedSinceLastDraw)
        {
            updateSplines();
        }

        for (Curve i : curves)
        {
            i.draw(image, step);
        }
    }

    public void updateCurves()
    {
        curves = new ArrayList<>();
        int lastIndex = 0;
        for (int i = 1; i < points.size() - 1; i++)
        {
            if (points.get(i).sharp)
            {
                curves.add(new Curve(points.subList(lastIndex, i + 1), color));
                lastIndex = i;
            }
        }

        if (!points.get(points.size() - 1).sharp)
        {
            curves.add(new Curve(points.subList(lastIndex, points.size()), color));
        }

        modifiedSinceLastDraw = false;
    }

    public void updateSplines()
    {
        if (modifiedSinceLastDraw)
        {
            updateCurves();
            return;
        }

        for (Curve i : curves)
        {
            i.updateSplines();
        }
    }

    public void toggleSharpness(int pointIndex)
    {
        points.get(pointIndex).sharp = !points.get(pointIndex).sharp;
        modifiedSinceLastDraw = true;
    }

    private void addPoint(Point point)
    {
        points.add(point);
        modifiedSinceLastDraw = true;
    }

    private void removePoint(int pointIndex)
    {
        points.remove(pointIndex);
        modifiedSinceLastDraw = true;
    }

    private void insertPoint(int pointIndex, Point point)
    {
        points.add(pointIndex, point);
        modifiedSinceLastDraw = true;
    }

    private void setPoint(int pointIndex, Point point)
    {
        points.set(pointIndex, point);
        modifiedSinceLastDraw = true;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public ArrayList<Point> getPoints()
    {
        return points;
    }
}
