package com.zackatoo.splineanimator;

import com.zackatoo.splineanimator.animation.curves.CircularCurve;
import com.zackatoo.splineanimator.animation.curves.Curve;
import com.zackatoo.splineanimator.animation.Point;
import com.zackatoo.splineanimator.animation.curves.PiecewiseCurve;
import marvin.image.MarvinImage;

public class ImageTest
{
    public static PiecewiseCurve getDefaultCurve()
    {
        Point[] points = new Point[5];
        points[0] = new Point(0,0, 1);
        points[1] = new Point(1150, 450, 1);
        points[2] = new Point(480, 480, 1);
        points[3] = new Point(1800, 1000, 1);
        points[4] = new Point(100, 950, 1);

        return new PiecewiseCurve(points, 0xFF000000);
    }

    public static MarvinImage getDefaultImage()
    {
        MarvinImage image = new MarvinImage(1920, 1080);
        image.clear(0xFFFFFFFF);
        return image;
    }

    public static MarvinImage test()
    {
        return curveTest();
    }

    private static MarvinImage squareSphereTest()
    {
        MarvinImage image = new MarvinImage(1920, 1080);
        image.clear(0xFFFFFFFF);

        Point[] points = new Point[8];
        points[0] = new Point(510, 10, 1);
        points[1] = new Point(1010, 10, 1);
        points[2] = new Point(1010, 510, 1);
        points[3] = new Point(1010, 1010, 1);
        points[4] = new Point(510, 1010, 1);
        points[5] = new Point(10, 1010, 1);
        points[6] = new Point(10, 510, 1);
        points[7] = new Point(10, 10,1);

        CircularCurve curve = new CircularCurve(points, 0xFF000000);
        curve.draw(image, 0.04);

        return image;
    }

    private static MarvinImage PerfectSphereTest()
    {
        MarvinImage image = new MarvinImage(1920, 1080);
        image.clear(0xFFFFFFFF);

        CircularCurve curve = new CircularCurve(new Point(510, 510, 1), 500, 0xFF000000);
        curve.draw(image, 0.04);

        return image;
    }

    private static MarvinImage badSphereTest()
    {
        MarvinImage image = new MarvinImage(1920, 1080);

        image.clear(0xFFFFFFFF);

        Point[] points = new Point[7];
        points[0] = new Point(510,10, 1);
        points[1] = new Point(511, 10, 1);
        points[2] = new Point(1010, 510, 1);
        points[3] = new Point(510, 1010, 1);
        points[4] = new Point(10, 510, 1);
        points[5] = new Point(509, 10, 1);
        points[6] = new Point(510, 10, 1);

        Curve curve = new Curve(points, 0xFF000000);
        curve.draw(image, 0.04);

        return image;
    }

    private static MarvinImage curveTest()
    {
        MarvinImage image = new MarvinImage(1920, 1080);

        image.clear(0xFFFFFFFF);

        Point[] points = new Point[5];
        points[0] = new Point(0,0, 1);
        points[1] = new Point(1150, 450, 1);
        points[2] = new Point(480, 480, 1);
        points[3] = new Point(1800, 1000, 1);
        points[4] = new Point(100, 950, 1);

        Curve curve = new Curve(points, 0xFF000000);
        curve.draw(image, 0.04);

        return image;
    }
}
