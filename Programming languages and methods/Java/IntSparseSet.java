import java.util.AbstractSet;
import java.util.Iterator;

public class IntSparseSet extends AbstractSet<Integer> {
    private int n;
    private int u;
    private int low;
    private int high;
    private int[] dence;
    private int[] sparse;
    public IntSparseSet(int low, int high) {
        n = 0;
        u = high - low;
        this.low = low;
        this.high = high;
        dence = new int[u];
        sparse = new int[u];
        for (int i = 0; i < u; i++) {
            sparse[i] = -1;
        }
    }

    public boolean contains(Integer x) {
        return ((sparse[x - low] >= 0) && (sparse[x - low] <= n) && (dence[sparse[x - low]] == x));
    }

    public boolean add(Integer y) {
        if ((y >= low) && (y < high) && (!this.contains(y))) {
            dence[n] = y;
            sparse[y - low] = n;
            n++;
            //System.out.println("Added " + y);
            return true;
        }
        return false;
    }

    public boolean remove(Object z) {
        if (((Integer)z >= low) && ((Integer)z < high) && (this.contains(z))) {
            if (n != 1) {
                dence[sparse[(Integer)z - low]] = dence[n - 1];
                sparse[dence[n - 1] - low] = sparse[(Integer)z - low];
            }
            n--;
            //System.out.println("Removed " + z);
            return true;
        }
        return false;
    }

    public void clear() {
        n = 0;
    }

    public int size() {
        return n;
    }

    public Iterator<Integer> iterator() {
        return new myIterator();
    }

    private class myIterator implements Iterator<Integer> {
        private int i = 0;

        public boolean hasNext() {
            return (i < n);
        }

        public Integer next() {
            i++;
            return dence[i - 1];
        }

        public void remove() {
            i--;
            IntSparseSet.this.remove(dence[i]);
        }
    }
}