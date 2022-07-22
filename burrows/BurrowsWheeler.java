import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static final int R = 256;

    public static void transform() {

        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);

        int first = 0;
        int length = input.length();

        for (int i = 0; i < length; i++) {
            if (csa.index(i) == 0) first = i;
        }
        BinaryStdOut.write(first);

        for (int i = 0; i < length; i++) {
            int lastIdx = (csa.index(i) - 1 + length) % length;
            BinaryStdOut.write(input.charAt(lastIdx));
        }

        BinaryStdOut.close();
    }

    public static void inverseTransform() {

        int first = BinaryStdIn.readInt();
        String lastCol = BinaryStdIn.readString();

        int length = lastCol.length();
        int[] next = new int[length];
        int[] count = new int[R + 1];
        char[] firstCol = new char[length];

        for (int i = 0; i < length; i++) {
            count[lastCol.charAt(i) + 1]++;
        }
        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }
        for (int i = 0; i < length; i++) {
            int j = count[lastCol.charAt(i)]++;
            firstCol[j] = lastCol.charAt(i);
            next[j] = i;
        }
        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(firstCol[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {

        if ("-".equals(args[0])) BurrowsWheeler.transform();
        if ("+".equals(args[0])) BurrowsWheeler.inverseTransform();
    }
}
