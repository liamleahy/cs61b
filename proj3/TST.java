import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Queue;
import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Collections;

/** This entire class is just a slightly editted version from Princeton TST!!! 
  *This is not my work.
  * Found here: http://algs4.cs.princeton.edu/52trie/TST.java.html 
  */
public class TST {
    public int N;              // size
    public Node root;   // root of TST

    public static class Node {
        public char c;                        // character
        public Node left, mid, right;  // left, middle, and right subtries
        public double val;                     // value associated with string
        public double max = 0.0;
        public String prefix;

        static class NodeMaxComparator implements Comparator<Node> {
            @Override
            public int compare(Node one, Node two) {
                double temp = two.max - one.max;
                if (temp > 0) return 1;
                return -1;
            }
        }

        static class NodeValComparator implements Comparator<Node> {
            @Override
            public int compare(Node one, Node two) {
                double temp = one.val - two.val;
                if (temp > 0) return 1;
                return -1;
            }
        }
    }

    public static class Wrapper {
        public double val;
        public String word;

        public Wrapper(Node x) {
            this.val = x.val;
            this.word = x.prefix;
        }

        public Wrapper(double val, String word) {
            this.val = val;
            this.word = word;
        }

        static class compr implements Comparator<Wrapper> {
            @Override
            public int compare(Wrapper one, Wrapper two) {
                double temp = one.val - two.val;
                if (temp > 0) return 1;
                else if (temp == 0) return 0;
                return -1;
            }
        }
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TST() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return N;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
     *     <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(String key) {
        return valueOf(key) != 0.0;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and <tt>null</tt> if the key is not in the symbol table
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public double valueOf(String key) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(root, key, 0);
        if (x == null) return 0.0;
        return x.val;
    }

    // return subtrie corresponding to given key
    public Node get(Node x, String key, int d) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        if (x == null) return null;
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is <tt>null</tt>, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void insert(String key, double val) {
        if (!contains(key)) N++;
        root = insert(root, key, val, 0);
    }

    private Node insert(Node x, String key, double val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
            x.prefix = key.substring(0, d + 1);
            x.max = val;
        }
        if      (x.max < val)           x.max     = val;
        if      (c < x.c)               x.left  = insert(x.left,  key, val, d);
        else if (c > x.c)               x.right = insert(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = insert(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of <tt>query</tt>,
     * or <tt>null</tt>, if no such string.
     * @param query the query string
     * @throws NullPointerException if <tt>query</tt> is <tt>null</tt>
     * @return the string in the symbol table that is the longest prefix of <tt>query</tt>,
     *     or <tt>null</tt> if no such string
     */
    public String longestPrefixOf(String query) {
        if (query == null || query.length() == 0) return null;
        int length = 0;
        Node x = root;
        int i = 0;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.val != 0.0) length = i;
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }

    /**
     * Returns all keys in the symbol table as an <tt>Iterable</tt>.
     * To iterate over all of the keys in the symbol table named <tt>st</tt>,
     * use the foreach notation: <tt>for (Key key : st.keys())</tt>.
     * @return all keys in the sybol table as an <tt>Iterable</tt>
     */
    public Iterable<String> keys() {
        Queue<String> queue = new LinkedList<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    /**
     * Returns all of the keys in the set that start with <tt>prefix</tt>.
     * @param prefix the prefix
     * @return all of the keys in the set that start with <tt>prefix</tt>,
     *     as an iterable
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> queue = new LinkedList<String>();
        Node x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != 0.0) queue.add(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }

    // public String strFinder(Node x, String prefix, PriorityQueue<Node> pq) {
    //     if (x == null) return prefix;
    //     if (x.val == x.max) return prefix + x.c;
    //     if (x.val != 0.0) pq.add(x);
    //     if (x.left.max == x.max) return strFinder(x.left, prefix, pq);
    //     if (x.right.max == x.max) return strFinder(x.right, prefix, pq);
    //     else return strFinder(x.mid, prefix + x.c, pq); 
            
    // }

    public String topMatch(String prefix) {
        Node x;
        if (prefix.length() == 0) x = root;
        else x = get(root, prefix, 0);
        if (x.val == x.max || x.mid == null) return prefix;
        return topMatch(x.mid, x.mid.max);
    }

    public String topMatch(Node x, double d) {
        if (x.val == d) return x.prefix;
        if (x.mid != null && x.mid.max == x.max) return topMatch(x.mid, d);
        if (x.left != null && x.left.max == x.max) return topMatch(x.left, d);
        else return topMatch(x.right, d);
    }

    public Iterable<String> reverse(PriorityQueue<Wrapper> ws) {
        Stack<String> s = new Stack<String>();
        while(ws.peek() != null) {
            s.push(ws.poll().word);
        }
        return s;
    }

    /**
     * Returns the first k of the keys in the set that start with <tt>prefix</tt>.
     * @param prefix the prefix
     * @return the first k of the keys in the set that start with <tt>prefix</tt>,
     *     as an iterable
     */
    public Iterable<String> topMatches(String prefix, int k) {
        PriorityQueue<Node> pqm = new PriorityQueue<Node>(k, new Node.NodeMaxComparator());
        PriorityQueue<Wrapper> tm = new PriorityQueue<Wrapper>(k, new Wrapper.compr());
        Node x;
        if (prefix.length() == 0) {
            x = root;
            pqm.offer(x);
        }
        else {
            x = get(root, prefix, 0);
            if (x != null && x.val != 0.0 && x.mid == null) {
                tm.add(new Wrapper(x));
                return reverse(tm);
            }
            if (x == null || x.mid == null) return reverse(tm);
            pqm.offer(x.mid);
        }
        if (x.val != 0.0) {
            tm.add(new Wrapper(x));
        }
        Wrapper rem = new Wrapper(0.0, "placeholder");
        tm.add(rem);
        while(pqm.peek() != null && pqm.peek().max > tm.peek().val) {
            Node temp = pqm.poll();
            if (temp.val != 0.0) {
                if (tm.size() == k) {
                    if (temp.val > tm.peek().val) {
                        tm.poll();
                        tm.add(new Wrapper(temp));
                    } 
                } else {
                    tm.add(new Wrapper(temp));
                }
            }
            if (temp.mid != null) {
                pqm.add(temp.mid);
            }
            if (temp.left != null) {
                pqm.add(temp.left);
            }
            if (temp.right != null) {
                pqm.add(temp.right);
            }
        }
        tm.remove(rem);
        return reverse(tm);
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.val != 0.0) queue.add(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }



    /**
     * Returns all of the keys in the symbol table that match <tt>pattern</tt>,
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match <tt>pattern</tt>,
     *     as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new LinkedList<String>();
        collect(root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }
 
    private void collect(Node x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) return;
        char c = pattern.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pattern, queue);
        if (c == '.' || c == x.c) {
            if (i == pattern.length() - 1 && x.val != 0.0) queue.add(prefix.toString() + x.c);
            if (i < pattern.length() - 1) {
                collect(x.mid, prefix.append(x.c), i+1, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pattern, queue);
    }


    /**
     * Unit tests the <tt>TST</tt> data type.
     */
    public static void main(String[] args) {

        // build symbol table from standard input
        TST st = new TST();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.insert(key, i);
        }

        // print results
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.keys()) {
                StdOut.println(key + " " + st.valueOf(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.longestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefix("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : st.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}
