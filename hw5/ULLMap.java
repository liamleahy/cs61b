import java.util.Set; /* java.util.Set needed only for challenge problem. */
import java.util.Iterator;
import java.util.NoSuchElementException;

/** A data structure that uses a linked list to store pairs of keys and values.
 *  Any key must appear at most once in the dictionary, but values may appear multiple
 *  times. Supports get(key), put(key, value), and contains(key) methods. The value
 *  associated to a key is the value in the last call to put with that key. 
 *
 *  For simplicity, you may assume that nobody ever inserts a null key or value
 *  into your map.
 */ 
public class ULLMap<K, V> implements Map61B<K, V>, Iterable<K> {  //FIX ME
    /** Keys and values are stored in a linked list of Entry objects.
      * This variable stores the first pair in this linked list. You may
      * point this at a sentinel node, or use it as a the actual front item
      * of the linked list. 
      */
    private Entry front;
    private int size = 0;

    @Override
    public V get(K key) { //FIX ME
    //FILL ME IN
        if (front == null) {
            return null;
        }
        Entry temp = front.get(key);
        if (temp == null) {
            return null;
        }
        return temp.val;

    }

    public static <K, V> ULLMap<V, K> invert(ULLMap<K, V> umo) {
        ULLMap<V, K> um = new ULLMap<V, K>();
        for (K k : umo) {
            um.put(umo.get(k), k);
        }
        return um;
    }

    @Override
    public void put(K key, V val) {
    	if (front != null) {
            Entry temp = front.get(key);
            if (temp == null) {
                front = new Entry(key, val, front);
            }
            else {
                temp.val = val;
            }
        } else {
            front = new Entry(key, val, front);
            size = size + 1;
        }
    }

    @Override
    public boolean containsKey(K key) { //FIX ME
    //FILL ME IN
        if (front == null) {
            return false;
        }
        return front.get(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
    	this.front = null;
    }

    public Iterator<K> iterator() {
    	return new UllMapIter();
    }

    public class UllMapIter implements Iterator<K> {

    	private Entry pointer;

    	public UllMapIter(){
    		pointer = front;
    	}

    	public boolean hasNext() {
    		return pointer != null;
    	}

    	public K next() {
			K k = pointer.key;
            pointer = pointer.next;
            return k;    	
        }

    	public void remove() {
    		throw new UnsupportedOperationException();
    	}
    }


    /** Represents one node in the linked list that stores the key-value pairs
     *  in the dictionary. */
    private class Entry {
    
        /** Stores KEY as the key in this key-value pair, VAL as the value, and
         *  NEXT as the next node in the linked list. */
        public Entry(K k, V v, Entry n) { //FIX ME
            key = k;
            val = v;
            next = n;
        }

        /** Returns the Entry in this linked list of key-value pairs whose key
         *  is equal to KEY, or null if no such Entry exists. */
        public Entry get(K k) { //FIX ME
            //FILL ME IN (using equals, not ==)
            if (k != null && k.equals(this.key)) {
                return this;
            } //FIX ME
            if (this.next == null) {
                return null;
            }
            return this.next.get(k);
        }

        /** Stores the key of the key-value pair of this node in the list. */
        private K key; //FIX ME
        /** Stores the value of the key-value pair of this node in the list. */
        private V val; //FIX ME
        /** Stores the next Entry in the linked list. */
        private Entry next;
    
    }

    /* Methods below are all challenge problems. Will not be graded in any way. 
     * Autograder will not test these. */
    @Override
    public V remove(K key) { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }


}