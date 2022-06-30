/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private final Point[] points;
    private LineSegment[] lineSegments;
    private int segmentCount;

    public BruteCollinearPoints(Point[] points) {

        assertNoNullNoRepeats(points);

        this.lineSegments = new LineSegment[2];
        this.points = points.clone();
        this.segmentCount = 0;

        Arrays.sort(this.points);

        for (int i = 0; i < this.points.length; i++) {
            for (int j = i + 1; j < this.points.length; j++) {
                for (int k = j + 1; k < this.points.length; k++) {
                    for (int ll = k + 1; ll < this.points.length; ll++) {
                        if (this.points[i].slopeTo(this.points[j]) == this.points[i]
                                .slopeTo(this.points[k]) &&
                                this.points[i].slopeTo(this.points[k]) == this.points[i]
                                        .slopeTo(this.points[ll])) {
                            addAndIncrease(new LineSegment(points[i], points[ll]));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return this.segmentCount;
    }

    public LineSegment[] segments() {
        LineSegment[] toReturn = Arrays.copyOf(this.lineSegments, this.segmentCount);
        return toReturn;
    }

    private void addAndIncrease(LineSegment ls) {

        if (this.segmentCount == this.lineSegments.length) {
            this.lineSegments = Arrays.copyOf(this.lineSegments, this.lineSegments.length * 2);
        }
        this.lineSegments[this.segmentCount++] = ls;
    }

    private void assertNoNullNoRepeats(Point[] p) {

        if (p == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p.length; j++) {
                if (p[i] == null) {
                    throw new IllegalArgumentException();
                }
                if (i != j && p[i].compareTo(p[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    public static void main(String[] args) {

        Point[] points = new Point[8];

        points[0] = new Point(10000, 0);
        points[1] = new Point(0, 10000);
        points[2] = new Point(3000, 7000);
        points[3] = new Point(7000, 3000);
        points[4] = new Point(20000, 21000);
        points[5] = new Point(3000, 4000);
        points[6] = new Point(14000, 15000);
        points[7] = new Point(6000, 7000);

        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        StdOut.println(bcp.numberOfSegments());
        for (LineSegment ls : bcp.segments()) {
            StdOut.println(ls);
        }
    }
}
