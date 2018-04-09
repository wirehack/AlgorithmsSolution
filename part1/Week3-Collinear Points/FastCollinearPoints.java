import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }
        Point[] sortedPoints = new Point[points.length];
        System.arraycopy(points, 0, sortedPoints, 0, points.length);
        Arrays.sort(sortedPoints);
        Point[] auxPoints = new Point[sortedPoints.length];
        System.arraycopy(sortedPoints, 0, auxPoints, 0, sortedPoints.length);
        lineSegments = new ArrayList<>();
        for (int p = 0; p < sortedPoints.length; p++) {
            System.arraycopy(sortedPoints, p + 1, auxPoints, p + 1, sortedPoints.length - (p + 1));
            Arrays.sort(auxPoints, p + 1, sortedPoints.length, sortedPoints[p].slopeOrder());
            if (p + 1 < sortedPoints.length && sortedPoints[p].compareTo(auxPoints[p + 1]) == 0)
                throw new IllegalArgumentException();
            for (int q = p + 1; q < auxPoints.length; q++) {
                int count = 0;
                while (q + 1 < auxPoints.length && sortedPoints[p].slopeTo(auxPoints[q]) == sortedPoints[p].slopeTo(auxPoints[q + 1])) {
                    count++;
                    q++;
                }
                if (count >= 2) {
                    lineSegments.add(new LineSegment(sortedPoints[p], auxPoints[q]));
                }
            }
        }
    }  // finds all line segments containing 4 or more points

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}