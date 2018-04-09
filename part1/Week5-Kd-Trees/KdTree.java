import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean xSplit;

        public Node(Point2D p, RectHV rect, boolean xSplit) {
            this.p = p;
            this.rect = rect;
            this.xSplit = xSplit;
        }

        public int compareToPoint(Point2D that) {
            if (p.equals(that))
                return 0;
            if (xSplit) {
                if (p.x() <= that.x()) return -1;
                else return 1;
            } else {
                if (p.y() <= that.y()) return -1;
                else return 1;
            }
        }
    }

    private Node root;
    private int count;

    public KdTree() {
    }   // construct an empty set of points

    public boolean isEmpty() {
        return root == null;
    }     // is the set empty?

    public int size() {
        return count;
    }     // number of points in the set

    public void insert(Point2D p) {
        validate(p);
        if (!contains(p)) {
            root = insert(root, p, 0, 0, 1, 1, true);
            count++;
        }
    }     // add the point to the set (if it is not already in the set)

    private Node insert(Node x, Point2D p, double xmin, double ymin, double xmax, double ymax, boolean xSplit) {
        if (x == null)
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax), xSplit);
        int cmp = x.compareToPoint(p);
        if (cmp > 0) {
            if (x.xSplit)
                x.lb = insert(x.lb, p, xmin, ymin, x.p.x(), ymax, false);
            else
                x.lb = insert(x.lb, p, xmin, ymin, xmax, x.p.y(), true);
        } else if (cmp < 0) {
            if (x.xSplit)
                x.rt = insert(x.rt, p, x.p.x(), ymin, xmax, ymax, false);
            else
                x.rt = insert(x.rt, p, xmin, x.p.y(), xmax, ymax, true);
        }
        return x;
    }

    public boolean contains(Point2D p) {
        validate(p);
        return get(root, p) != null;
    }     // does the set contain point p?

    private Node get(Node x, Point2D p) {
        if (x == null) return null;
        int cmp = x.compareToPoint(p);
        if (cmp > 0) return get(x.lb, p);
        else if (cmp < 0) return get(x.rt, p);
        else return x;
    }

    public void draw() {
        if (root != null) {
            drawNodes(root);
        }
    }

    private void drawNodes(Node node) {
        node.p.draw();
        if (node.lb != null) {
            drawNodes(node.lb);
        }
        if (node.rt != null) {
            drawNodes(node.rt);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        List<Point2D> pointList = new ArrayList<>();
        range(root, rect, pointList);
        return pointList;
    }     // all points that are inside the rectangle (or on the boundary)

    private void range(Node x, RectHV rect, List<Point2D> pointList) {
        if (x == null) return;
        else if (!x.rect.intersects(rect)) return;
        else {
            if (rect.contains(x.p))
                pointList.add(x.p);
            range(x.lb, rect, pointList);
            range(x.rt, rect, pointList);
        }
    }

    public Point2D nearest(Point2D p) {
        validate(p);
        return nearest(root, p, Double.POSITIVE_INFINITY);
    }     // a nearest neighbor in the set to point p; null if the set is empty

    private Point2D nearest(Node x, Point2D p, double min) {
        if (x == null) return null;
        if (x.rect.distanceTo(p) > min) return null;
        double distance = x.p.distanceTo(p);
        boolean less = false;
        if (distance < min) {
            min = distance;
            less = true;
        }
        int cmp = x.compareToPoint(p);
        Point2D t1, t2;
        if (cmp > 0) {
            t1 = nearest(x.lb, p, min);
            if (t1 != null)
                t2 = nearest(x.rt, p, t1.distanceTo(p));
            else
                t2 = nearest(x.rt, p, min);
        } else {
            t1 = nearest(x.rt, p, min);
            if (t1 != null)
                t2 = nearest(x.lb, p, t1.distanceTo(p));
            else
                t2 = nearest(x.lb, p, min);
        }
        if (t2 != null)
            return t2;
        else if (t1 != null)
            return t1;
        else if (less)
            return x.p;
        else return null;
    }


    private void validate(Object o) {
        if (o == null)
            throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        Point2D p1 = new Point2D(.7000, .2000);
        Point2D p2 = new Point2D(.5000, .4000);
        Point2D p3 = new Point2D(.2000, .3000);
        Point2D p4 = new Point2D(.4000, .7000);
        Point2D p5 = new Point2D(.9000, .6000);

        tree.insert(p1);
        tree.insert(p2);
        tree.insert(p3);
        tree.insert(p4);
        tree.insert(p5);
        Point2D nearest = tree.nearest(new Point2D(0.7, 0.42));
        StdOut.println("nearest:" + nearest);
    }     // unit testing of the methods (optional)

}
