import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {

    private final int length;
    private final char[] chars;
    private final Integer[] indices;

    public CircularSuffixArray(String s) {

        if (s == null) throw new IllegalArgumentException();

        length = s.length();
        chars = new char[length];
        indices = new Integer[length];

        for (int i = 0; i < length; i++) {
            indices[i] = i;
            chars[i] = s.charAt(i);
        }

        Arrays.sort(indices, new Comparator<Integer>() {
            public int compare(Integer first, Integer second) {
                for (int i = 0; i < length; i++) {

                    char c1 = chars[(i + first) % length];
                    char c2 = chars[(i + second) % length];
                    if (c1 > c2) return 1;
                    else if (c1 < c2) return -1;
                }
                return 0;
            }
        });
    }

    public int length() {
        return length;
    }

    public int index(int i) {
        if (i < 0 || i >= length) throw new IllegalArgumentException();
        return indices[i];
    }

    public static void main(String[] args) {

        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        System.out.println(csa.length());
        System.out.println(csa.index(2));
    }
}
