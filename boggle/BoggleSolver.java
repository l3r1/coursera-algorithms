import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BoggleSolver {

    private static class BoggleTrie {

        private static final int R = 26;
        private Node root;
        private final HashMap<String, Node> prefixNodes;

        private static class Node {
            private Integer val;
            private final Node[] next = new Node[R];
        }

        private BoggleTrie() {
            prefixNodes = new HashMap<>();
        }

        private void put(String key) {
            this.root = put(root, key, 1, 0);
        }

        private Node put(Node node, String key, int val, int depth) {

            if (node == null) node = new Node();
            if (depth == key.length()) {
                node.val = val;
                return node;
            }
            int c = key.charAt(depth) - 65;
            node.next[c] = put(node.next[c], key, val, depth + 1);
            return node;
        }

        private boolean keysWithPrefix(String s) {

            Node n = prefixNodes.get(s);
            if (n != null) return true;

            int N = s.length();

            if (N > 0) {
                n = prefixNodes.get(s.substring(0, N - 1));
                if (n != null) {
                    return existPrefix(n, s, N - 1);
                }
            }
            return existPrefix(root, s, 0);
        }

        private boolean existPrefix(Node n, String s, int depth) {

            if (n == null) return false;

            for (int i = depth; i < s.length(); i++) {
                n = n.next[s.charAt(i) - 65];
                if (n == null) return false;
            }
            if (n.val != null) {
                prefixNodes.put(s, n);
                return true;
            }
            for (char c = 0; c < R; c++) {
                if (n.next[c] != null) {
                    prefixNodes.put(s, n);
                    return true;
                }
            }
            return false;
        }
    }

    private static final int[][] MOVE = {
            {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };
    private final BoggleTrie trie;
    private final ArrayList<String> dict;
    private ArrayList<String> validWords;

    public BoggleSolver(String[] dictionary) {

        if (dictionary == null) throw new IllegalArgumentException();

        trie = new BoggleTrie();
        dict = new ArrayList<>();

        for (String str : dictionary) {
            trie.put(str);
            dict.add(str);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {

        if (board == null) throw new IllegalArgumentException();
        validWords = new ArrayList<>();

        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                findWords(board, row, col);
            }
        }
        return validWords;
    }

    private void findWords(BoggleBoard board, int row, int col) {

        StringBuilder sb = new StringBuilder();
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        findWords(board, visited, sb, row, col);
    }

    private void findWords(BoggleBoard board, boolean[][] visited, StringBuilder sb, int row, int col) {

        if (row < 0 || row >= board.rows() || col < 0 || col >= board.cols()) return;
        if (visited[row][col]) return;
        visited[row][col] = true;

        sb.append(board.getLetter(row, col));
        String s = sb.toString().replace("Q", "QU");

        if (trie.keysWithPrefix(s)) {
            if (s.length() >= 3 && dict.contains(s)) {
                validWords.add(s);
            }
            for (int[] move : MOVE) {
                findWords(board, visited, sb, row + move[0], col + move[1]);
            }
        }

        visited[row][col] = false;
        sb.deleteCharAt(sb.length() - 1);
    }

    public int scoreOf(String word) {

        if (word == null) throw new IllegalArgumentException();

        int N = word.length();
        if (!dict.contains(word) || N < 3) return 0;

        switch (N) {
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 4;
        }
        return 11;
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

    }
}
