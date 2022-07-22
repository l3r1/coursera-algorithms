import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    public static void encode() {

        char[] arr = initArray();

        String in = BinaryStdIn.readString();
        for (int i = 0; i < in.length(); i++) {
            swap(arr, in.charAt(i), true);
        }
        BinaryStdOut.close();
    }

    public static void decode() {

        char[] arr = initArray();

        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readInt(8);
            BinaryStdOut.write(arr[i]);
            swap(arr, arr[i], false);
        }
        BinaryStdOut.close();
    }

    private static char[] initArray() {

        char[] a = new char[R];
        for (int i = 0; i < R; i++) {
            a[i] = (char) i;
        }
        return a;
    }

    private static void swap(char[] arr, char c, boolean print) {

        char first = arr[0];
        for (int i = 0; i < R; i++) {
            if (arr[i] == c) {
                arr[0] = c;
                arr[i] = first;
                if (print) BinaryStdOut.write((char) i);
                break;
            }
            char temp = arr[i];
            arr[i] = first;
            first = temp;
        }
    }

    public static void main(String[] args) {

        if ("-".equals(args[0])) MoveToFront.encode();
        else if ("+".equals(args[0])) MoveToFront.decode();
    }
}
