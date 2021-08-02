import java.util.*;

public class SkipList<K extends Comparable<K>,V> extends AbstractMap<K,V> {
    private int levels;
    private List l;
    private int size;

    public SkipList(int levels) {
        this.levels = levels;
        l = new List();
        size = 0;
    }

    public boolean isEmpty() {
        return (l.next(0) == null);
    }

    public int size() {
        return size;
    }

    public List succ(List l) {
        return l.next(0);
    }

    public void skip(List l, K k, List[] p) {
        List x = l;
        int i = levels - 1;
        while (i >= 0) {
            while ((x.next(i) != null) && (x.next(i).getKey().compareTo(k) < 0)) {
                x = x.next(i);
            }
            p[i] = x;
            i--;
        }
    }

    public boolean containsKey(Object key) {
        K k = (K) key;
        List[] p = new List[levels];
        skip(l, k, p);
        List x = succ(p[0]);
        return ((x != null) && (x.getKey().equals(k)));
    }

    public V get(Object key) {
        K k = (K) key;
        List[] p = new List[levels];
        skip(l, k, p);
        List x = succ(p[0]);
        if ((x == null) || (!x.getKey().equals(k))) {
            return null;
        }
        return (V)x.getValue();
    }

    public V put(K k, V v) {
        List[] p = new List[levels];
        skip(l, k, p);
        V result = null;
        if ((p[0].next(0) != null) && (p[0].next(0).getKey().equals(k))) {
            result = (V)succ(p[0]).getValue();
            size--;
        }
        List x = new List(k, v);
        final Random random = new Random();
        int r = random.nextInt() * 2;
        int i = 0;
        while ((i < levels) && (r % 2 == 0)) {
            x.set(i, p[i].next(i));
            p[i].set(i, x);
            i++;
            r /= 2;
        }
        while (i < levels) {
            x.set(i, null);
            i++;
        }
        size++;
        return result;
    }

    public V remove(Object key) {
        K k = (K) key;
        List[] p = new List[levels];
        skip(l, k, p);
        V result = null;
        List x = succ(p[0]);
        if ((x == null) || (!x.getKey().equals(k))) {
            return null;
        } else {
            result = (V) x.getValue();
            int i = 0;
            while ((i < levels) && (p[i].next(i) == x)) {
                p[i].set(i, x.next(i));
                i++;
            }
            size--;
            return result;
        }
    }

    public void clear() {
        size = 0;
        l = new List();
    }

    public Set<Entry<K, V>> entrySet() {
        return new SkipListSet();
    }

    class List<K extends Comparable<K>, V> implements Map.Entry<K, V> {
        private K key;
        private V value;
        private List[] next;

        public List() {
            next = new List[levels];
        }

        public List(K k, V v) {
            key = k;
            value = v;
            next = new List[levels];
        }

        public List next(int i) {
            return next[i];
        }

        public void set(int i, List lst) {
            next[i] = lst;
        }

        public V getValue() {
            return value;
        }

        public K getKey() {
            return key;
        }

        public V setValue(V v) {
            value = v;
            return value;
        }
    }

    class SkipListSet <K,V> extends AbstractSet {

        public Iterator iterator() {
            return new myIterator();
        }

        public int size() {
            return size;
        }

        private class myIterator implements Iterator {
            private List x;

            public myIterator() {
                x = l;
            }

            public boolean hasNext() {
                return (x.next(0) != null);
            }

            public Map.Entry<K, V> next() {
                x = x.next(0);
                return x;
            }

            public void remove() {
                SkipList.this.remove(x.getKey());
            }
        }
    }
}