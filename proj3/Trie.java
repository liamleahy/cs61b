import java.util.HashMap;

/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Liam Leahy
 */
public class Trie {
    private Node tn; //dah root!
    private static final int SIZE = 256;

    /** Makes a new Trie */
    public Trie() {
        tn = new Node();
    }

    /** Access method for root Node. 
      * @return Node the root Node.
      */
    public Node getNode() {
        return tn;
    }

    /** Finds s
      * @param s String to be found.
      * @param isFullWord Boolean that specifies if this is listed as a full word.
      * @return boolean that evaluates whether or not String s is in Trie.
      */
    public boolean find(String s, boolean isFullWord) {
        return tn.contains(s, isFullWord);
    }

    /** Adds s to trie 
      * @param s String to be inserted into Trie.
      */
    public void insert(String s) {
        if (s.equals(null) || s.equals("")) {
            throw new IllegalArgumentException();
        }
        tn.insert(s);
    }

    /** Supporting node class for the trie (where the real work is done). */
    public class Node {
        /** This is an array of Nodes that is supposed to have one slot
          * for every ascii symbol. */
        private HashMap<Character, Node> _curr = new HashMap<Character, Node>(2);
        private boolean _end = false; //if this is the end of a word 
        private boolean _hasMore = false;

        /** Access method for aray of Nodes. 
          * @return Node[] the Node[] for all ascii.
        */
        public HashMap<Character, Node> getCurr() {
            return _curr;
        }

        /** Access method for boolean indicating end of word. 
          * @return boolean whether or not this is the end of a word.
        */
        public boolean end() {
            return _end;
        }

        /** Constructs a new Node */
        public Node() {
        }

        /** Inserts the string word into this Node 
          * @param word String that is to be inserted into the Node. 
          */
        public void insert(String word) {
            Node current = this;
            String temp = word;
            while (temp.length() > 1) {
                Character c = temp.charAt(0);
                if (current._curr.get(c) == null) {
                    current._curr.put(c, new Node());
                }
                current._curr.get(c)._hasMore = true;
                temp = temp.substring(1);
                current = current._curr.get(c);            
            }
            Character c = temp.charAt(0);
            if (current._curr.get(c) == null) {
                current._curr.put(c, new Node());    
            }
            current._curr.get(c)._end = true;
        }

        /** Returns whether or not this Node contains 
          * the substring word and if _end agrees with end.
          * Assumes the word is not null or empty string.
          * @param word String that is being checked.
          * @param end Boolean that specifies whether or not it is the end of a word.
          * @return boolean returns whether or not it contains word.
          */
        public boolean contains(String word, boolean end) {
            if (word.length() == 0) { //checks if this is the last letter
                return ((_end == end) || (_hasMore != end));
            }
            Character c = word.charAt(0);
            String sub = word.substring(1);
            return (_curr.get(c) != null && _curr.get(c).contains(sub, end));
        }
    }
}












