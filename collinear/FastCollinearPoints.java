/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private Point[] points;
    private int segTracker = 0;
    private int segmentSize = 10;
    private LineSegment[] segmentArr = new LineSegment[segmentSize];

    public FastCollinearPoints(Point[] points) {

        assertNoNullNoRepeats(points);

        this.points = points.clone();
        ArrayList<Integer> currentSegmentList = new ArrayList<Integer>();

        for (int i = 0; i < this.points.length; i++) {

            Point temp = this.points[i];
            this.points[i] = this.points[0];
            this.points[0] = temp;
            Point[] sorted = this.points.clone();
            Arrays.sort(sorted, 1, this.points.length, temp.slopeOrder());

            double prevSlope = temp.slopeTo(sorted[1]);
            int segLenTracker = 1;
            currentSegmentList.clear();
            currentSegmentList.add(0);
            currentSegmentList.add(1);
            int minSeg = 2;
            for (int k = 2; k < this.points.length; k++) {
                double newSlope = temp.slopeTo(sorted[k]);
                if (prevSlope != newSlope || k == this.points.length - 1) {

                    Point lastPoint = sorted[k - 1];
                    if (k == this.points.length - 1 && prevSlope == newSlope) {
                        lastPoint = sorted[k];
                        currentSegmentList.add(k);
                        segLenTracker++;
                    }

                    if (segLenTracker > minSeg) {
                        Point maxPoint = temp;
                        Point minPoint = temp;

                        for (int innerCurSegTracker : currentSegmentList) {
                            if (sorted[innerCurSegTracker].compareTo(maxPoint) > 0)
                                maxPoint = sorted[innerCurSegTracker];

                            if (sorted[innerCurSegTracker].compareTo(minPoint) <= 0)
                                minPoint = sorted[innerCurSegTracker];
                        }


                        if (segTracker == segmentArr.length) {
                            resizeSegmentArr();
                        }

                        if (temp.compareTo(minPoint) == 0) {

                            segmentArr[segTracker] = new LineSegment(minPoint, maxPoint);
                            segTracker++;
                        }
                    }
                    segLenTracker = 1;
                    currentSegmentList.clear();
                    currentSegmentList.add(0);
                    currentSegmentList.add(k);

                }
                else {
                    segLenTracker++;
                    currentSegmentList.add(k);
                }
                prevSlope = newSlope;

            }

        }


    }

    private void resizeSegmentArr() {
        LineSegment[] temp = new LineSegment[segmentArr.length * 2];
        for (int resizeTracker = 0; resizeTracker < segmentArr.length;
             resizeTracker++) {
            temp[resizeTracker] = segmentArr[resizeTracker];
        }
        segmentArr = temp;
    }

    public int numberOfSegments() {
        return segTracker;
    }

    public LineSegment[] segments() {
        LineSegment[] segmentCloned = new LineSegment[segTracker];
        int cloneTracker = 0;
        for (LineSegment segment : segmentArr) {
            if (segment != null) {
                segmentCloned[cloneTracker] = segment;
                cloneTracker++;
            }
        }
        return segmentCloned;
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

        Point[] p = new Point[8];
        p[0] = new Point(10000, 0);
        p[1] = new Point(0, 10000);
        p[2] = new Point(3000, 7000);
        p[3] = new Point(7000, 3000);
        p[4] = new Point(20000, 21000);
        p[5] = new Point(3000, 4000);
        p[6] = new Point(14000, 15000);
        p[7] = new Point(6000, 7000);

        Arrays.sort(p, p[0].slopeOrder());
        for (Point po : p) {
            StdOut.println(po);
            StdOut.println(po.slopeTo(p[0]));
        }

        FastCollinearPoints fcp = new FastCollinearPoints(p);
        StdOut.println(fcp.numberOfSegments());
        for (LineSegment ls : fcp.segments()) {
            StdOut.println(ls);
        }
    }
}
