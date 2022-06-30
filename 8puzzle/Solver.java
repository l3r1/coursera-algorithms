/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {

    private final Board board;
    private int moves;
    private boolean solvable;
    private final ArrayList<Board> solution;

    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException();
        this.board = initial;
        this.moves = 0;
        this.solution = new ArrayList<>();
        this.findSolution();
    }

    public boolean isSolvable() {
        return this.solvable;
    }

    public int moves() {
        if (!this.isSolvable()) return -1;
        else return this.moves;
    }

    public Iterable<Board> solution() {
        if (!this.isSolvable()) return null;
        else return this.solution;
    }

    private void findSolution() {

        MinPQ<SearchNode> algo = new MinPQ<>();
        MinPQ<SearchNode> twin = new MinPQ<>();

        boolean solved = false;
        boolean solvedTwin = false;

        algo.insert(new SearchNode(this.board, this.moves, null));
        twin.insert(new SearchNode(this.board.twin(), this.moves, null));

        while (!solved && !solvedTwin) {
            SearchNode lastTwin = twin.delMin();
            SearchNode last = algo.delMin();

            if (lastTwin.getBoard().isGoal()) {
                solvedTwin = true;
                this.solvable = false;
            }
            if (last.getBoard().isGoal()) {
                solved = true;
                this.solvable = true;
                this.moves = last.getMoves();
                this.solution.add(last.getBoard());
                if (last.getPrevious() != null) {
                    SearchNode back = last.getPrevious();
                    while (back.getPrevious() != null) {
                        this.solution.add(back.getPrevious().getBoard());
                        back = back.getPrevious();

                    }
                }
                this.solution.add(this.board);
                Collections.reverse(this.solution);
            }
            for (Board b : last.getBoard().neighbors()) {
                if (last.getPrevious() == null || !b.equals(last.getPrevious().getBoard())) {
                    algo.insert(new SearchNode(b, last.getMoves() + 1, last));
                }
            }
            for (Board b : lastTwin.getBoard().neighbors()) {
                if (lastTwin.getPrevious() == null || !b
                        .equals(lastTwin.getPrevious().getBoard())) {
                    twin.insert(new SearchNode(b, lastTwin.getMoves() + 1, lastTwin));
                }
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {

        private final Board board;
        private final int moves;
        private final int priority;
        private final SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.priority = moves + board.manhattan();
            this.previous = previous;
        }

        public SearchNode getPrevious() {
            return this.previous;
        }

        public int getPriority() {
            return this.priority;
        }

        public Board getBoard() {
            return this.board;
        }

        public int getMoves() {
            return this.moves;
        }

        public int compareTo(SearchNode other) {
            return Integer.compare(this.priority, other.getPriority());
        }
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
