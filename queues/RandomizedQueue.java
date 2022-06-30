/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] q;
    private int size;

    public RandomizedQueue() {

        this.q = (Item[]) new Object[1];
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (this.size == q.length) {
            Item[] newQ = (Item[]) new Object[q.length * 2];
            for (int i = 0; i < q.length; i++) {
                newQ[i] = q[i];
            }
            this.q = newQ;
        }

        this.q[this.size] = item;
        this.size++;
    }

    public Item dequeue() {

        if (this.size == 0) throw new NoSuchElementException();

        int random = this.getRandomItem();
        Item toDq = this.q[random];

        this.q[random] = null;
        this.size--;

        if (this.q.length > 4 && this.size <= q.length / 4) {
            Item[] resizedQ = (Item[]) new Object[q.length / 2];
            for (int i = 0; i < this.size; i++) {
                resizedQ[i] = q[i];
            }
            this.q = resizedQ;
        }

        return toDq;
    }

    public Item sample() {

        if (this.size == 0) throw new NoSuchElementException();

        return this.q[this.getRandomItem()];
    }

    private int getRandomItem() {
        while (true) {
            int random = StdRandom.uniform(this.size);
            if (this.q[random] != null) {
                return random;
            }
        }
    }

    public Iterator<Item> iterator() {

        return new QIterator(this.q, this.size());
    }

    private class QIterator implements Iterator<Item> {

        private final Item[] iteratorQ;
        private int current;

        public QIterator(Item[] q, int size) {

            this.iteratorQ = (Item[]) new Object[size];
            this.current = 0;

            for (int i = 0; i < iteratorQ.length; i++) {
                iteratorQ[i] = q[i];
            }

            for (int j = 0; j < iteratorQ.length; j++) {

                int swapTo = StdRandom.uniform(j + 1);

                Item temp = iteratorQ[j];
                iteratorQ[j] = iteratorQ[swapTo];
                iteratorQ[swapTo] = temp;
            }
        }

        @Override
        public boolean hasNext() {
            return this.current < iteratorQ.length;
        }

        @Override
        public Item next() {
            if (!this.hasNext()) throw new NoSuchElementException();

            Item item = iteratorQ[current];
            current++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {

        RandomizedQueue<String> q = new RandomizedQueue<>();
        StdOut.println(q.isEmpty());
        StdOut.println(q.size());

        q.enqueue("Bob");
        q.enqueue("Jack");
        q.enqueue("Sam");
        q.enqueue("Rick");
        q.enqueue("Amy");
        StdOut.println(q.isEmpty());
        StdOut.println(q.size());

        StdOut.println(q.dequeue());
        StdOut.println(q.dequeue());
        StdOut.println(q.size());

        StdOut.println(q.sample());
        StdOut.println(q.size());

        StdOut.println(q.iterator().hasNext());
        StdOut.println(q.iterator().next());

    }
}
