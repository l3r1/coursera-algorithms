/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public double slopeTo(Point that) {

        if (that.x - this.x == 0 && that.y - this.y == 0) {
            return Double.NEGATIVE_INFINITY;
        }
        else if (that.x - this.x == 0) {
            return Double.POSITIVE_INFINITY;
        }
        else if (that.y - this.y == 0) {
            return +0.0;
        }
        else return (double) (that.y - this.y) / (that.x - this.x);

    }

    public int compareTo(Point that) {
        if (this.y < that.y || (this.y == that.y && this.x < that.x)) return -1;
        if (this.y == that.y && this.x == that.x) return 0;
        return 1;
    }

    public Comparator<Point> slopeOrder() {

        return new Comparator<Point>() {
            public int compare(Point o1, Point o2) {
                if (o1.slopeTo(Point.this) < o2.slopeTo(Point.this)) return -1;
                else if (o1.slopeTo(Point.this) > o2.slopeTo(Point.this)) return 1;
                return 0;
            }
        };
    }


    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static void main(String[] args) {

        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 5);
        Point p3 = new Point(5, 0);

        StdOut.println(p1.compareTo(p2));
        StdOut.println(p1.compareTo(p3));
        StdOut.println(p2.compareTo(p3));

        StdOut.println(p1.slopeTo(p2));
        StdOut.println(p1.slopeTo(p3));
        StdOut.println(p2.slopeTo(p3));
        StdOut.println(p2.slopeTo(p2));

        StdOut.println(new Point(4, 4).slopeOrder().compare(new Point(4, 5), new Point(3, 4)));
    }
}
