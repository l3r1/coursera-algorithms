import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> points;

    public PointSET() {
        this.points = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return this.points.isEmpty();
    }

    public int size() {
        return this.points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        this.points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return this.points.contains(p);
    }

    public void draw() {
        for (Point2D p : this.points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        ArrayList<Point2D> range = new ArrayList<Point2D>();

        for (Point2D p : this.points) {
            if (rect.contains(p)) {
                range.add(p);
            }
        }
        return range;
    }

    public Point2D nearest(Point2D p) {

        if (p == null) throw new IllegalArgumentException();
        if (this.points.isEmpty()) return null;

        Point2D nearest = this.points.first();
        for (Point2D pp : this.points) {
            if (pp.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                nearest = pp;
            }
        }
        return nearest;
    }


    public static void main(String[] args) {

        PointSET points = new PointSET();
        points.insert(new Point2D(10, 10));
        points.insert(new Point2D(1, 1));
        points.insert(new Point2D(2, 2));
        System.out.println(points.size());
        points.draw();
    }
}
