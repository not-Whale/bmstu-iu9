import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;

public class SparseSet<T extends Hintable> extends AbstractSet<T> {

    private int n;
    private ArrayList<T> dence;

    public SparseSet() {
        n = 0;
        dence = new ArrayList<>();
    }

    public boolean contains(Object x) {
        if ((((T)x).hint() >= n) || (((T)x).hint() < 0)) {
            return false;
        }
        return (((T)x) == dence.get(((T)x).hint()));
    }

    public boolean add(T x) {
        if (!contains(x)) {
            x.setHint(n);
            dence.add(x);
            n++;
            return true;
        }
        return false;
    }

    public boolean remove(Object x) {
        if (contains(x)) {
            n--;
            dence.set(((T)x).hint(), dence.get(n));
            dence.get(n).setHint(((T)x).hint());
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

    public Iterator<T> iterator() {
        return new myIterator();
    }

    private class myIterator implements Iterator<T> {
        private int i = 0;

        public boolean hasNext() {
            return (i < n);
        }

        public T next() {
            i++;
            return dence.get(i - 1);
        }

        public void remove() {
            i--;
            SparseSet.this.remove(dence.get(i));
        }
    }
}
