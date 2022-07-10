package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph dg;
    private int length;
    private int nearestAncestor;
    private int[] distTo1;
    private int[] distTo2;
    private boolean[] marked1;
    private boolean[] marked2;
    private final Stack<Integer> stack1;
    private final Stack<Integer> stack2;

    public SAP(Digraph G) {

        if (G == null) throw new IllegalArgumentException();
        this.dg = G;
        this.distTo1 = new int[G.V()];
        this.distTo2 = new int[G.V()];
        this.marked1 = new boolean[G.V()];
        this.marked2 = new boolean[G.V()];
        this.stack1 = new Stack<Integer>();
        this.stack2 = new Stack<Integer>();
    }

    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        compute(v, w);
        return this.length;
    }

    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        compute(v, w);
        return this.nearestAncestor;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        compute(v, w);
        return this.length;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        compute(v, w);
        return this.nearestAncestor;
    }

    private void compute(int v, int w) {

        this.length = -1;
        this.nearestAncestor = -1;
        this.distTo1[v] = 0;
        this.distTo2[w] = 0;
        this.marked1[v] = true;
        this.marked2[w] = true;
        this.stack1.push(v);
        this.stack2.push(w);
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        q1.enqueue(v);
        q2.enqueue(w);
        bfs(q1, q2);
    }

    private void compute(Iterable<Integer> v, Iterable<Integer> w) {

        this.length = -1;
        this.nearestAncestor = -1;
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        for (int v1 : v) {
            marked1[v1] = true;
            stack1.push(v1);
            distTo1[v1] = 0;
            q1.enqueue(v1);
        }
        for (int w1 : w) {
            marked2[w1] = true;
            stack2.push(w1);
            distTo2[w1] = 0;
            q2.enqueue(w1);
        }
        bfs(q1, q2);
    }

    private void bfs(Queue<Integer> q1, Queue<Integer> q2) {

        while (!q1.isEmpty() || !q2.isEmpty()) {
            if (!q1.isEmpty()) {
                int v = q1.dequeue();
                if (marked2[v]) {
                    if (distTo1[v] + distTo2[v] < length || length == -1) {
                        this.nearestAncestor = v;
                        length = distTo1[v] + distTo2[v];
                    }
                }

                if (distTo1[v] < length || length == -1) {
                    for (int w : this.dg.adj(v)) {
                        if (!marked1[w]) {
                            distTo1[w] = distTo1[v] + 1;
                            marked1[w] = true;
                            stack1.push(w);
                            q1.enqueue(w);
                        }
                    }
                }
            }
            if (!q2.isEmpty()) {
                int v = q2.dequeue();
                if (marked1[v]) {
                    if (distTo1[v] + distTo2[v] < length || length == -1) {
                        this.nearestAncestor = v;
                        length = distTo1[v] + distTo2[v];
                    }
                }

                if (distTo2[v] < length || length == -1) {
                    for (int w : this.dg.adj(v)) {
                        if (!marked2[w]) {
                            distTo2[w] = distTo2[v] + 1;
                            marked2[w] = true;
                            stack2.push(w);
                            q2.enqueue(w);
                        }
                    }
                }
            }
        }
        init();
    }


    private void init() {

        while (!this.stack1.isEmpty()) {
            int v = this.stack1.pop();
            this.marked1[v] = false;
        }
        while (!this.stack2.isEmpty()) {
            int v = this.stack2.pop();
            this.marked2[v] = false;
        }
    }

    private void validateVertex(int v) {

        int V = this.marked1.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    private void validateVertices(Iterable<Integer> vertices) {

        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = this.marked1.length;
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
            }
        }
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
