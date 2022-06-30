import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    private Node root;

    private class Node {

        private final int depth;
        private Point2D point;
        private Node right;
        private Node left;
        private int size;

        public Node(Point2D p, int depth) {
            this.point = p;
            this.depth = depth;
            this.size = 1;
        }

    }

    public KdTree() {

    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size(this.root);
    }

    private int size(Node n) {
        if (n == null) return 0;
        return n.size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            this.root = insert(this.root, p, 1);
        }
    }

    private Node insert(Node n, Point2D p, int depth) {

        if (n == null) {
            return new Node(p, depth);
        }

        int compare = compare(n, p);

        if (compare < 0) {
            n.left = insert(n.left, p, depth + 1);
        }
        else if (p.compareTo(n.point) == 0) {
            n.point = p;
        }
        else {
            n.right = insert(n.right, p, depth + 1);
        }
        // int compareX = Double.compare(p.x(), n.point.x());
        // int compareY = Double.compare(p.y(), n.point.y());
        //
        // if (n.depth % 2 != 0) {
        //
        //     if (compareX < 0) {
        //         n.left = insert(n.left, p, depth + 1);
        //     }
        //     else if (compareX > 0) {
        //         n.right = insert(n.right, p, depth + 1);
        //     }
        //     else {
        //         n.point = p;
        //     }
        // }
        // else {
        //
        //     if (compareY < 0) {
        //         n.left = insert(n.left, p, depth + 1);
        //     }
        //     else if (compareY > 0) {
        //         n.right = insert(n.right, p, depth + 1);
        //     }
        //     else {
        //         n.point = p;
        //     }
        // }
        n.size = size(n.left) + size(n.right) + 1;
        return n;
    }

    public boolean contains(Point2D p) {

        if (p == null) throw new IllegalArgumentException();

        Node currentRoot = this.root;

        while (currentRoot != null) {

            int compare = compare(currentRoot, p);

            if (compare < 0) {
                currentRoot = currentRoot.left;
            }
            else if (p.compareTo(currentRoot.point) == 0) {
                return true;
            }
            else {
                currentRoot = currentRoot.right;
            }
        }
        return false;
    }

    private int compare(Node first, Point2D second) {

        if (first.depth % 2 != 0) {
            return Double.compare(second.x(), first.point.x());
        }
        else {
            return Double.compare(second.y(), first.point.y());
        }
    }

    public void draw() {
        draw(this.root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node n, RectHV line) {

        if (n == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        n.point.draw();

        if (n.depth % 2 != 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), line.ymin(), n.point.x(), line.ymax());
            draw(n.left, new RectHV(line.xmin(), line.ymin(), n.point.x(), line.ymax()));
            draw(n.right, new RectHV(n.point.x(), line.ymin(), line.xmax(), line.ymax()));
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(line.xmin(), n.point.y(), line.xmax(), n.point.y());
            draw(n.left, new RectHV(line.xmin(), line.ymin(), line.xmax(), n.point.y()));
            draw(n.right, new RectHV(line.xmin(), n.point.y(), line.xmax(), line.ymax()));
        }
    }

    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> range = new ArrayList<Point2D>();
        range(rect, range, this.root);
        return range;
    }

    private void range(RectHV rect, ArrayList<Point2D> range, Node n) {

        if (n == null) return;

        if (rect.contains(n.point)) {
            range.add(n.point);
        }

        if (n.depth % 2 != 0) {
            if (rect.xmin() <= n.point.x()) {
                range(rect, range, n.left);
            }
            if (rect.xmax() >= n.point.x()) {
                range(rect, range, n.right);
            }
        }
        else {
            if (rect.ymin() <= n.point.y()) {
                range(rect, range, n.left);
            }
            if (rect.ymax() >= n.point.y()) {
                range(rect, range, n.right);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        return nearest(p, this.root.point, this.root);
    }

    private Point2D nearest(Point2D p, Point2D currentNearest, Node n) {

        if (n == null) return currentNearest;

        if (n.depth % 2 != 0) {
            if (p.x() > n.point.x()) {
                Point2D xRight = nearest(p, n.point.distanceSquaredTo(p) < currentNearest
                        .distanceSquaredTo(p) ? n.point : currentNearest, n.right);
                if (xRight.distanceSquaredTo(p) > Math.abs(n.point.x() - p.x())) {
                    Point2D xLeft = nearest(p, xRight, n.left);
                    return xRight.distanceSquaredTo(p) > xLeft.distanceSquaredTo(p) ? xLeft :
                           xRight;
                }
                else {
                    return xRight;
                }
            }
            else {
                Point2D xLeft = nearest(p, n.point.distanceSquaredTo(p) < currentNearest
                        .distanceSquaredTo(p) ? n.point : currentNearest, n.left);
                if (xLeft.distanceSquaredTo(p) > Math.abs(n.point.x() - p.x())) {
                    Point2D xRight = nearest(p, xLeft, n.right);
                    return xRight.distanceSquaredTo(p) > xLeft.distanceSquaredTo(p) ? xLeft :
                           xRight;
                }
                else {
                    return xLeft;
                }
            }
        }
        else {
            if (p.y() > n.point.y()) {
                Point2D yUp = nearest(p, n.point.distanceSquaredTo(p) < currentNearest
                        .distanceSquaredTo(p) ? n.point : currentNearest, n.right);
                if (yUp.distanceSquaredTo(p) > Math.abs(n.point.y() - p.y())) {
                    Point2D yDown = nearest(p, yUp, n.left);
                    return yUp.distanceSquaredTo(p) > yDown.distanceSquaredTo(p) ? yDown : yUp;
                }
                else {
                    return yUp;
                }
            }
            else {
                Point2D yDown = nearest(p, n.point.distanceSquaredTo(p) < currentNearest
                        .distanceSquaredTo(p) ? n.point : currentNearest, n.left);
                if (yDown.distanceSquaredTo(p) > Math.abs(n.point.y() - p.y())) {
                    Point2D yUp = nearest(p, yDown, n.right);
                    return yUp.distanceSquaredTo(p) > yDown.distanceSquaredTo(p) ? yDown : yUp;
                }
                else {
                    return yDown;
                }
            }

        }
    }

    public static void main(String[] args) {


        KdTree kd = new KdTree();

        kd.insert(new Point2D(0.7, 0.2));
        kd.insert(new Point2D(0.5, 0.4));
        kd.insert(new Point2D(0.2, 0.3));
        kd.insert(new Point2D(0.4, 0.7));
        kd.insert(new Point2D(0.9, 0.6));

        System.out.println(kd.size());
        System.out.println(kd.nearest(new Point2D(0.226, 0.528)));
        System.out.println(new Point2D(0.226, 0.528).distanceSquaredTo(new Point2D(0.4, 0.7)));
        System.out.println(new Point2D(0.226, 0.528).distanceSquaredTo(new Point2D(0.2, 0.3)));
    }
}
