import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF ufVirtual;
    private WeightedQuickUnionUF ufNoBottom;
    private int gridSize;
    private int virtualTop;
    private int virtualBottom;
    private int openSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        }

        this.gridSize = n;
        this.grid = new boolean[n][n];

        this.virtualTop = n * n;
        this.virtualBottom = n * n + 1;
        this.ufVirtual = new WeightedQuickUnionUF(n * n + 2);
        this.ufNoBottom = new WeightedQuickUnionUF(n * n + 1);

        this.openSites = 0;
    }

    public void open(int row, int col) {

        this.validate(row, col);

        if (!isOpen(row, col)) {

            this.grid[row - 1][col - 1] = true;
            int index = this.getIndex(row, col);
            this.openSites++;

            if (row == 1) {
                this.ufVirtual.union(this.virtualTop, index);
                this.ufNoBottom.union(this.virtualTop, index);
            }

            if (row == this.gridSize) {
                this.ufVirtual.union(this.virtualBottom, index);
            }

            if (this.isValid(row, col - 1) && this.isOpen(row, col - 1)) {
                this.ufVirtual.union(index, this.getIndex(row, col - 1));
                this.ufNoBottom.union(index, this.getIndex(row, col - 1));
            }

            if (this.isValid(row, col + 1) && this.isOpen(row, col + 1)) {
                this.ufVirtual.union(index, this.getIndex(row, col + 1));
                this.ufNoBottom.union(index, this.getIndex(row, col + 1));
            }

            if (this.isValid(row - 1, col) && this.isOpen(row - 1, col)) {
                this.ufVirtual.union(index, this.getIndex(row - 1, col));
                this.ufNoBottom.union(index, this.getIndex(row - 1, col));
            }

            if (this.isValid(row + 1, col) && this.isOpen(row + 1, col)) {
                this.ufVirtual.union(index, this.getIndex(row + 1, col));
                this.ufNoBottom.union(index, this.getIndex(row + 1, col));
            }
        }
    }

    public boolean isOpen(int row, int col) {

        this.validate(row, col);
        return this.grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {

        this.validate(row, col);
        return (this.ufNoBottom.find(this.virtualTop) == this.ufNoBottom
                .find(this.getIndex(row, col)));
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        return (this.ufVirtual.find(this.virtualTop) == this.ufVirtual.find(this.virtualBottom));
    }

    private void validate(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Arguments are out of bounds");
        }
    }

    private boolean isValid(int row, int col) {
        return (row - 1 >= 0 && col - 1 >= 0
                && row - 1 < this.gridSize && col - 1 < this.gridSize);
    }

    private int getIndex(int row, int col) {
        return (this.gridSize * (row - 1) + col) - 1;
    }

    public static void main(String[] args) {
    }
}
