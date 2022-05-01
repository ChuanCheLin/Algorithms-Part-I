/* *****************************************************************************
 *  Name:              Eric Lin
 *  Last modified:     February 18, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int segnum = 0;
    private LineSegment[] lines;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
        if (points == null || isNull(points)) {
            throw new IllegalArgumentException();
        }
        Arrays.sort(points);
        if (isRepeatedPoint(points)) {
            throw new IllegalArgumentException();
        }


        lines = new LineSegment[points.length];

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int p = k + 1; p < points.length; p++) {
                        if (Double
                                .compare(points[i].slopeTo(points[j]), points[j].slopeTo(points[k]))
                                == 0 &&
                                Double.compare(points[j].slopeTo(points[k]),
                                               points[k].slopeTo(points[p])) == 0) {
                            lines[segnum++] = new LineSegment(points[i], points[p]);
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
