package wordnet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wn;

    public Outcast(WordNet wordnet) {

        if (wordnet == null) {
            throw new IllegalArgumentException("arguments to Outcast() is null");
        }
        this.wn = wordnet;
    }

    public String outcast(String[] nouns) {

        if (nouns == null) {
            throw new IllegalArgumentException("arguments to outcast() is null");
        }

        int id = -1;
        int max = -1;
        int[] distSum = new int[nouns.length];

        for (int i = 0; i < nouns.length; i++) {
            for (String noun : nouns) {
                distSum[i] += this.wn.distance(nouns[i], noun);
            }
            if (distSum[i] > max) {
                max = distSum[i];
                id = i;
            }
        }
        if (id == -1) {
            throw new IllegalArgumentException("error");
        }
        return nouns[id];
    }

    public static void main(String[] args) {

        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
