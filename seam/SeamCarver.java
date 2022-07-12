import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {

    private int width;
    private int height;
    private int[][] picture;
    private double[][] energy;
    private final SeamFinder sf;
    private boolean flipped;


    public SeamCarver(Picture picture) {

        if (picture == null) throw new IllegalArgumentException();

        this.height = picture.height();
        this.width = picture.width();

        this.picture = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                this.picture[row][col] = picture.getRGB(col, row);
            }
        }

        this.energy = new double[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                this.energy[row][col] = energy(col, row);
            }
        }
        this.sf = new SeamFinder();
    }

    private void flip() {

        int temp = this.height;
        this.height = this.width;
        this.width = temp;

        int[][] pictureFlipped = new int[height][width];
        double[][] energyFlipped = new double[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pictureFlipped[row][col] = this.picture[col][row];
                energyFlipped[row][col] = this.energy[col][row];
            }
        }
        this.picture = pictureFlipped;
        this.energy = energyFlipped;
        this.flipped = !this.flipped;
    }

    public Picture picture() {

        if (this.flipped) flip();

        Picture pic = new Picture(this.width, this.height);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pic.set(col, row, new Color(this.picture[row][col]));
            }
        }
        return pic;
    }

    public int width() {
        if (this.flipped) return this.height;
        return this.width;
    }

    public int height() {
        if (this.flipped) return this.width;
        return this.height;
    }

    public double energy(int x, int y) {

        if (this.flipped) {
            int temp = x;
            x = y;
            y = temp;
        }

        validateInBound(x, y);
        if (y == 0 || x == 0 || y == this.height - 1 || x == this.width - 1) return 1000.0;

        double xGrad = getGrad(this.picture[y][x - 1], this.picture[y][x + 1]);
        double yGrad = getGrad(this.picture[y - 1][x], this.picture[y + 1][x]);

        return Math.sqrt(xGrad + yGrad);
    }

    private double getGrad(int x, int y) {

        int r = ((x >> 16) & 0x0ff) - ((y >> 16) & 0x0ff);
        int g = ((x >> 8) & 0x0ff) - ((y >> 8) & 0x0ff);
        int b = (x & 0x0ff) - (y & 0x0ff);

        return r * r + g * g + b * b;
    }

    public int[] findVerticalSeam() {

        if (this.flipped) flip();
        return this.sf.findSeam();
    }

    public int[] findHorizontalSeam() {

        if (!this.flipped) flip();
        return this.sf.findSeam();
    }

    public void removeVerticalSeam(int[] seam) {

        if (this.flipped) flip();

        if (seam == null || this.width <= 1) throw new IllegalArgumentException();
        validateSeam(seam);

        width--;

        for (int row = 0; row < height; row++) {
            System.arraycopy(picture[row], seam[row] + 1, picture[row], seam[row], width - seam[row]);
            System.arraycopy(energy[row], seam[row] + 1, energy[row], seam[row], width - seam[row]);
        }

        for (int row = 1; row < height; row++) {
            int cut = seam[row];
            if (cut > 0) energy[row][cut - 1] = energy(cut - 1, row);
            if (cut < width) energy[row][cut] = energy(cut, row);
        }
    }

    public void removeHorizontalSeam(int[] seam) {

        if (!this.flipped) flip();

        if (seam == null || this.height <= 1) throw new IllegalArgumentException();
        validateSeam(seam);

        width--;
        for (int row = 0; row < height; row++) {
            System.arraycopy(picture[row], seam[row] + 1, picture[row], seam[row], width - seam[row]);
            System.arraycopy(energy[row], seam[row] + 1, energy[row], seam[row], width - seam[row]);
        }
        for (int row = 1; row < height; row++) {
            int cut = seam[row];
            if (cut > 0) energy[row][cut - 1] = energy(row, cut - 1);
            if (cut < width) energy[row][cut] = energy(row, cut);
        }
    }

    private void validateInBound(int col, int row) {
        if (col < 0 || col >= width || row < 0 || row >= height) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSeam(int[] seam) {

        if (seam.length != height) throw new IllegalArgumentException();

        for (int row = 0; row < height; row++) {
            if (seam[row] < 0 || seam[row] >= width) {
                throw new IllegalArgumentException();
            }
        }

        int colPrev = seam[0];
        for (int row = 1; row < height; row++) {
            int col = seam[row];
            if (Math.abs(col - colPrev) > 1) {
                throw new IllegalArgumentException();
            }
            colPrev = col;
        }
    }

    private class SeamFinder {

        private static final double INF = Double.POSITIVE_INFINITY;

        public SeamFinder() {
        }

        public int[] findSeam() {

            Stack<Integer> seamCols = new Stack<>();
            double[][] distTo = new double[height][width];
            int[][] colTo = new int[height][width];

            Arrays.fill(distTo[0], 0);
            for (int row = 1; row < height; row++) {
                Arrays.fill(distTo[row], INF);
            }

            for (int row = 0; row < height - 1; row++) {
                for (int col = 0; col < width; col++) {
                    relax(col, row, distTo, colTo);
                }
            }

            int col = 0;
            double minDist = INF;

            for (int j = 0; j < width; j++) {
                if (minDist > distTo[height - 1][j]) {
                    minDist = distTo[height - 1][j];
                    col = colTo[height - 1][j];
                }
            }

            seamCols.push(col);
            for (int i = height - 1; i > 0; i--) {
                col = colTo[i][col];
                seamCols.push(col);
            }

            int[] seam = new int[height];
            int i = 0;
            for (int seamCol : seamCols) {
                seam[i] = seamCol;
                i++;
            }
            return seam;
        }

        private void relax(int col, int row, double[][] distTo, int[][] colTo) {

            if (col > 0) {

                if (distTo[row + 1][col - 1] == INF || distTo[row + 1][col - 1] > distTo[row][col] + energy[row + 1][col - 1]) {
                    distTo[row + 1][col - 1] = distTo[row][col] + energy[row + 1][col - 1];
                    colTo[row + 1][col - 1] = col;
                }
            }

            if (distTo[row + 1][col] == INF || distTo[row + 1][col] > distTo[row][col] + energy[row + 1][col]) {
                distTo[row + 1][col] = distTo[row][col] + energy[row + 1][col];
                colTo[row + 1][col] = col;
            }

            if (col < width - 1) {
                if (distTo[row + 1][col + 1] == INF || distTo[row + 1][col + 1] > distTo[row][col] + energy[row + 1][col + 1]) {
                    distTo[row + 1][col + 1] = distTo[row][col] + energy[row + 1][col + 1];
                    colTo[row + 1][col + 1] = col;
                }
            }
        }
    }

    public static void main(String[] args) {

        SeamCarver s = new SeamCarver(new Picture("6x5.png"));
        System.out.println(s.energy(1, 2));

        for (double[] arr : s.energy) {
            System.out.println(Arrays.toString(arr));
        }

        System.out.println(Arrays.toString(s.findVerticalSeam()));
        System.out.println(Arrays.toString(s.findHorizontalSeam()));
    }
}
