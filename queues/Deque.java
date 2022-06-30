/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    public Deque() {
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {

        if (item == null) throw new IllegalArgumentException();

        Node node = new Node();
        node.item = item;

        this.size++;

        if (this.size == 1) {
            this.first = node;
            this.last = node;
        }
        else {
            Node prevFirst = this.first;
            this.first = node;
            node.next = prevFirst;
            prevFirst.previous = node;
        }
    }

    public void addLast(Item item) {

        if (item == null) throw new IllegalArgumentException();

        Node node = new Node();
        node.item = item;

        this.size++;

        if (this.size == 1) {
            this.first = node;
            this.last = node;
        }
        else {
            Node prevLast = this.last;
            this.last = node;
            node.previous = prevLast;
            prevLast.next = node;
        }
    }

    public Item removeFirst() {

        if (size() == 0) throw new NoSuchElementException();

        Item item = this.first.item;
        this.size--;

        this.first = this.first.next;

        if (size() == 0) {
            this.last = null;
        }
        else {
            this.first.previous = null;
        }

        return item;
    }

    public Item removeLast() {
        if (size() == 0) throw new NoSuchElementException();

        Item item = this.last.item;
        this.size--;

        this.last = this.last.previous;

        if (size() == 0) {
            this.first = null;
        }
        else {
            this.last.next = null;
        }

        return item;
    }

    public Iterator<Item> iterator() {

        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;

            return item;
        }

        public void remove(Item item) {
            throw new UnsupportedOperationException();
        }
    }


    public static void main(String[] args) {

        Deque<Integer> d = new Deque<>();

        StdOut.println(d.isEmpty());
        StdOut.println(d.size());

        for (int i = 0; i < 10; i++) {
            d.addFirst(i);
        }

        StdOut.println(d.isEmpty());
        StdOut.println(d.size());
        StdOut.println(d.first.item);
        StdOut.println(d.last.item);

        d.addFirst(99);
        d.addLast(100);
        StdOut.println(d.first.item);
        StdOut.println(d.last.item);

        d.removeFirst();
        d.removeLast();
        StdOut.println(d.size());
        StdOut.println(d.first.item);
        StdOut.println(d.last.item);

        Iterator<Integer> iterator = d.iterator();
        StdOut.println(iterator.next());
        StdOut.println(iterator.next());
    }
}
