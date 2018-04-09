import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointSet;

    public PointSET() {
        pointSet = new TreeSet<>();
    }    // construct an empty set of points

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }    // is the set empty?

    public int size() {
        return pointSet.size();
    }    // number of points in the set

    public void insert(Point2D p) {
        validate(p);
        pointSet.add(p);
    }    // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        validate(p);
        return pointSet.contains(p);
    }    // does the set contain point p?

    public void draw() {
        for (Point2D point : pointSet) {
            point.draw();
        }
    }    // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        List<Point2D> pointList = new ArrayList<>();
        for (Point2D point : pointSet) {
            if (rect.contains(point))
                pointList.add(point);
        }
        return pointList;
    }    // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        validate(p);
        if (isEmpty())
            return null;
        double min = Double.POSITIVE_INFINITY;
        Point2D near = null;
        double distance;
        for (Point2D point : pointSet) {
            distance = point.distanceTo(p);
            if (distance < min) {
                min = distance;
                near = point;
            }
        }
        return near;
    }    // a nearest neighbor in the set to point p; null if the set is empty

    private void validate(Object o) {
        if (o == null)
            throw new IllegalArgumentException();

    }

    public static void main(String[] args) {
    }    // unit testing of the methods (optional)
}