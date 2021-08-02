public class Element<T> {
    private T x;
    public static int depth;
    private Element parent;
    public Element(T t) {
        x = t;
        depth = 0;
        parent = this;
    }

    public T x() {
        return x;
    }

    public static int getDepth() {
        return depth;
    }

    public void union(Element<T> elem) {
        Element rootx = Find(this);
        Element rooty = Find(elem);
        if (rootx.getDepth() < rooty.getDepth()) {
            rootx.parent = rooty;
        } else {
            rooty.parent = rootx;
            if ((rootx.depth == rooty.depth) && (rootx != rooty)) {
                rootx.depth++;
            }
        }
    }

    public boolean equivalent(Element<T> elem) {
        return (Find(this) == Find(elem));
    }

    public static Element Find(Element x) {
        Element root;
        if (x.parent == x) {
            root = x;
        } else {
            x.parent = Find(x.parent);
            root = x.parent;
        }
        return root;
    }
}