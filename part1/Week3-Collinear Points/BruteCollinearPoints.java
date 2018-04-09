import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }
        Point[] sortedPoints=new Point[points.length];
        System.arraycopy(points, 0, sortedPoints, 0, points.length);
        Arrays.sort(sortedPoints);
        lineSegments = new ArrayList<>();
        for (int p = 0; p < sortedPoints.length; p++)
            for (int q = p + 1; q < sortedPoints.length; q++) {
                double slopePQ = sortedPoints[p].slopeTo(sortedPoints[q]);
                if (slopePQ == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException();
                }
                for (int r = q + 1; r < sortedPoints.length; r++) {
                    double slopePR = sortedPoints[p].slopeTo(sortedPoints[r]);
                    if (slopePR == Double.NEGATIVE_INFINITY) {
                        throw new IllegalArgumentException();
                    }
                    if (slopePQ == slopePR) {
                        for (int s = r + 1; s < sortedPoints.length; s++) {
                            double slopePS = sortedPoints[p].slopeTo(sortedPoints[s]);
                            if (slopePS == Double.NEGATIVE_INFINITY) {
                                throw new IllegalArgumentException();
                            }
                            if (slopePR == slopePS) {
                                lineSegments.add(new LineSegment(sortedPoints[p], sortedPoints[s]));
                            }
                        }
                    }
                }
            }// finds all line segments containing 4 points
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }   // the number of line segments

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }   // the line segments
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