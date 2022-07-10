package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Topological;

public class WordNet {

    private Digraph hypernyms;
    private ST<String, SET<Integer>> synsets;
    private ST<Integer, String> idNouns;
    private int idSum;
    private int outSum;
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        this.readSynsets(synsets);
        this.readHypernyms(hypernyms);

    }

    private void readSynsets(String synsets) {

        this.synsets = new ST<>();
        this.idNouns = new ST<>();

        In synsetsIn = new In(synsets);

        while (synsetsIn.hasNextLine()) {

            this.idSum++;

            String[] split = synsetsIn.readLine().split(",");
            int id = Integer.parseInt(split[0]);
            this.idNouns.put(id, split[1]);

            String[] nouns = split[1].split(" ");

            for (String noun : nouns) {
                if (this.synsets.contains(noun)) {
                    this.synsets.get(noun).add(id);
                } else {
                    SET<Integer> ids = new SET<>();
                    ids.add(id);
                    this.synsets.put(noun, ids);
                }
            }
        }
    }

    private void readHypernyms(String hypernyms) {

        this.hypernyms = new Digraph(idSum);
        In hypernymsIn = new In(hypernyms);
        boolean[] outEdge = new boolean[idSum];

        while (hypernymsIn.hasNextLine()) {

            String[] split = hypernymsIn.readLine().split(",");
            int v = Integer.parseInt(split[0]);

            for (int i = 1; i < split.length; i++) {
                int w = Integer.parseInt(split[1]);
                this.hypernyms.addEdge(v, w);
            }
            if (!outEdge[v] && split.length != 1) {
                this.outSum++;
            }
            outEdge[v] = true;
        }
        isRootedDAG();
        this.sap = new SAP(this.hypernyms);
    }

    private void isRootedDAG() {
        if (this.idSum - this.outSum != 1) {
            throw new IllegalArgumentException();
        }
        Topological topo = new Topological(this.hypernyms);
        if (!topo.hasOrder()) {
            throw new IllegalArgumentException();
        }
    }

    public Iterable<String> nouns() {
        return this.synsets.keys();
    }

    public boolean isNoun(String word) {

        if (word == null) throw new IllegalArgumentException();
        return synsets.contains(word);
    }

    public int distance(String nounA, String nounB) {

        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        SET<Integer> setA = this.synsets.get(nounA);
        SET<Integer> setB = this.synsets.get(nounB);
        if (setA.size() == 1 && setB.size() == 1) {
            return this.sap.length(setA.max(), setB.max());
        } else {
            return this.sap.length(setA, setB);
        }
    }

    public String sap(String nounA, String nounB) {

        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        int id;

        SET<Integer> setA = this.synsets.get(nounA);
        SET<Integer> setB = this.synsets.get(nounB);
        if (setA.size() == 1 && setB.size() == 1) {
            id = this.sap.ancestor(setA.max(), setB.max());
        } else {
            id = this.sap.ancestor(setA, setB);
        }
        return this.idNouns.get(id);
    }

    public static void main(String[] args) {
        //penis
    }
}
