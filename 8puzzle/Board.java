/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final int[] board;
    private final int size;

    public Board(int[][] tiles) {
        if (tiles == null || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException();
        }
        this.size = tiles.length;
        this.board = new int[this.size * this.size];

        int i = 0;
        for (int[] row : tiles) {
            for (int col : row) {
                board[i] = col;
                i++;
            }
        }
    }

    private int[] getBoard() {
        return this.board;
    }

    public String toString() {

        StringBuilder toReturn = new StringBuilder();
        toReturn.append(this.size);
        for (int i = 0; i < this.board.length; i++) {
            if (i % this.size == 0) {
                toReturn.append("\n" + " ");
            }
            toReturn.append(this.board[i] + "  ");
        }
        return toReturn.toString().trim();
    }

    public int dimension() {
        return this.size;
    }

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < this.board.length; i++) {
            if (board[i] != i + 1 && board[i] != 0) {
                hamming++;
            }
        }
        return hamming;
    }

    public int manhattan() {

        int manhattan = 0;

        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] != i + 1 && this.board[i] != 0) {
                int[] target = this.calcGoal(this.board[i]);
                int row = i / this.size;
                int col = i % this.size;
                int distance = Math.abs(target[0] - row) + Math.abs(target[1] - col);
                manhattan += distance;
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        if (this.board[this.board.length - 1] != 0) {
            return false;
        }
        for (int i = 0; i < this.board.length - 1; i++) {
            if (this.board[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object y) {
        if (y == null || this.getClass() != y.getClass()) {
            return false;
        }
        if (this == y) {
            return true;
        }
        return Arrays.equals(this.getBoard(), ((Board) y).getBoard());
    }

    public Iterable<Board> neighbors() {

        ArrayList<Board> neighbors = new ArrayList<Board>();
        ArrayList<Integer> moves = new ArrayList<>();
        int zeroIdx;

        for (zeroIdx = 0; zeroIdx < this.board.length; zeroIdx++) {
            if (this.board[zeroIdx] == 0) {

                int row = calcLoc(zeroIdx)[0];
                int col = calcLoc(zeroIdx)[1];

                if (validMove(row - 1, col)) {
                    moves.add(calcIndex(row - 1, col));
                }
                if (validMove(row + 1, col)) {
                    moves.add(calcIndex(row + 1, col));
                }
                if (validMove(row, col - 1)) {
                    moves.add(calcIndex(row, col - 1));
                }
                if (validMove(row, col + 1)) {
                    moves.add(calcIndex(row, col + 1));
                }
                break;
            }
        }

        for (int i : moves) {
            int[] temp = this.board.clone();
            swap(temp, zeroIdx, i);
            int[][] tempTiles = make1Into2(temp, this.size);
            Board b = new Board(tempTiles);
            neighbors.add(b);
        }
        return neighbors;
    }

    public Board twin() {

        int[] temp = this.board.clone();

        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != 0) {
                int row = calcLoc(i)[0];
                int col = calcLoc(i)[1];
                if (validMove(row - 1, col)) {
                    swap(temp, i, calcIndex(row - 1, col));
                    break;
                }
                if (validMove(row + 1, col)) {
                    swap(temp, i, calcIndex(row + 1, col));
                    break;
                }
                if (validMove(row, col - 1)) {
                    swap(temp, i, calcIndex(row, col - 1));
                    break;
                }
                if (validMove(row, col + 1)) {
                    swap(temp, i, calcIndex(row, col + 1));
                    break;
                }
            }
        }
        return new Board(this.make1Into2(temp, this.size));
    }

    private int[] calcLoc(int num) {
        return new int[] { num / this.size, num % this.size };
    }

    private int[] calcGoal(int num) {
        if (num == 0) {
            return new int[] { this.size - 1, this.size - 1 };
        }

        int row = num / this.size;
        if (num % this.size == 0) row = num / this.size - 1;
        if (row < 0) row = 0;

        int col = num - (row * this.size) - 1;
        return new int[] { row, col };
    }

    private int calcIndex(int row, int col) {
        return (row * this.size) + col;
    }

    private boolean validMove(int row, int col) {
        return (row >= 0 && row < this.size && col >= 0 && col < this.size);
    }

    private void swap(int[] arr, int swap, int swapee) {
        int temp = arr[swap];
        arr[swap] = arr[swapee];
        arr[swapee] = temp;
    }

    private int[][] make1Into2(int[] arr, int size) {
        int[][] twoD = new int[size][size];
        for (int i = 0; i < arr.length; i++) {
            int x = calcLoc(i)[0];
            int y = calcLoc(i)[1];
            twoD[x][y] = arr[i];
        }
        return twoD;
    }

    public static void main(String[] args) {

        Board b = new Board(new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 },
                });

        Board b2 = new Board(new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 },
                });

        Board b3 = new Board(new int[][] {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 }
        });

        System.out.println(b.toString());
        System.out.print("\n");

        System.out.println(b.dimension());
        System.out.print("\n");

        System.out.println(b.hamming());
        System.out.print("\n");

        System.out.println(b.manhattan());
        System.out.print("\n");

        System.out.println(b.isGoal());
        System.out.println(b.equals(b2));
        System.out.print("\n");

        System.out.println(b.neighbors());
        System.out.print("\n");

        System.out.println(b.twin());
        System.out.println(b.twin());

        System.out.println(b3.isGoal());
    }
}
