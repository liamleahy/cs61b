import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node _root;

    private class Node {
        private K _key;
        private V _val;
        private Node _left, _right;
        private int _N = 0;

        public Node(K key, V val, int N) {
            this(key, val, N, null, null);
        }

        public Node(K key, V val, int N, Node left, Node right) {
            _key = key;
            _val = val;
            _N = N;
            _left = left;
            _right = right;
        }
    }

    public int size() {
        return size(_root);
    }

    public int size(Node n) {
        if (n == null) {
            return 0;
        }
        return n._N;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public BSTMap(K key, V val) {
        _root = new Node(key, val, 1);
    }

    public BSTMap() {
        _root = null;
    }

    public BSTMap(K key, V val, Node left, Node right) {
        _root = new Node(key, val, 1 + size(left) + size(right), left, right);
    }

    public void clear() {
        _root = null;
    }

    public V get(Node n, K key) {
        if (n == null) {
            return null;
        } else if (key.compareTo(n._key) > 0) {
            return get(n._right, key);
        } else if (key.compareTo(n._key) < 0) {
            return get(n._left, key);
        }
        else {
            return n._val;
        }
    }

    public V get(K key) {
        return get(_root, key);
    }

    public Node put(Node n, K key, V val) {
        if (n == null) {
            return new Node(key, val, 1);
        } else if (key.compareTo(n._key) > 0) {
            n._right = put(n._right, key, val);
        } else if (key.compareTo(n._key) < 0) {
            n._left = put(n._left, key, val);
        }
        n._N += 1;
        return n;
    }

    public void put(K key, V val) {
       _root = put(_root, key, val);
    }

    public void printInOrder() {
        printInOrder(_root);
    }

    public void printInOrder(Node n) {
        if (n == null) {
            return;
        }
        if (n._left != null) {
            printInOrder(n._left);
        }
        System.out.println(n._key);
        if (n._right != null) {
            printInOrder(n._right);
        }
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    public V remove (K key, V val) {
        throw new UnsupportedOperationException();        
    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException();        
    }
}
