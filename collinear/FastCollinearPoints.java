/* *****************************************************************************
 *  Name:              Eric Lin
 *  Last modified:     February 19, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private int segnum = 0;
    private LineSegment[] lines;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null || isNull(points)) {
            throw new IllegalArgumentException();
        }
        Arrays.sort(points);
        if (isRepeatedPoint(points)) {
            throw new IllegalArgumentException();
        }

        Point[] a = new Point[points.length];
        duplicate(points, a);

        lines = new LineSegment[points.length];
        for (int i = 0; i < points.length; i++) {
            Arrays.sort(points, a[i].slopeOrder());
            int count = 0;
            Point min = a[i];
            Point max = a[i];
            for (int j = 1; j < points.length; j++) {
                if (Double.compare(a[i].slopeTo(points[j]), a[i].slopeTo(points[j - 1])) == 0) {
                    count++;
                    if (points[j].compareTo(min) < 0) {
                        min = points[j];
                    }
                    if (points[j - 1].compareTo(min) < 0) {
                        min = points[j - 1];
                    }
                    if (points[j].compareTo(max) > 0) {
                        max = points[j];
                    }
                    if (points[j - 1].compareTo(max) > 0) {
                        max = points[j - 1];
                    }
                    if (j == points.length - 1 && count > 1 && min.compareTo(a[i]) == 0) {
                        lines[segnum++] = new LineSegment(a[i], max);
                    }
                }
                else if (count > 1 && min.compareTo(a[i]) == 0) {
                    lines[segnum++] = new LineSegment(a[i], max);
                    count = 0;
                    min = a[i];
                    max = a[i];
                }
                else {
                    count = 0;
                    min = a[i];
                    max = a[i];
                }
            }
        }
    }

    private boolean isRepeatedPoint(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isNull(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                return true;
        }
        return false;
    }

    // duplicate a into b
    private void duplicate(Point[] a, Point[] b) {
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i];
        }
    }


    public int numberOfSegments() { // the number of line segments
        return segnum;
    }

    public LineSegment[] segments() { // the line segments
        LineSegment[] duplicate = new LineSegment[segnum];
        for (int i = 0; i < segnum; i++) {
            duplicate[i] = lines[i];
        }
        return duplicate;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.01);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
