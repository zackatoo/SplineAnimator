package com.zackatoo.splineanimator.animation;

public class Point
{
    public int x;
    public int y;
    public int thickness = 1;
    public boolean sharp = false;

    public Point(int x, int y, int thickness)
    {
        this.x = x;
        this.y = y;
        this.thickness = thickness;
    }

    public Point(double x, double y, double thickness)
    {
        this.x = (int)Math.round(x);
        this.y = (int)Math.round(y);
        this.thickness = (int)Math.round(thickness);
    }

    public Point(String line, int lineNum)
            throws Exception
    {
        String[] fields = line.split(",", -1);
        if (fields.length == 1) throw new Exception("Line " + lineNum + " must have at least two fields to be a point");
        x = Integer.parseInt(fields[0]);
        y = Integer.parseInt(fields[1]);
        if (fields.length > 2)
        {
            thickness = Integer.parseInt(fields[2]);
        }
        if (fields.length > 3)
        {
            sharp = fields[3].equals("1");
        }
    }
}
