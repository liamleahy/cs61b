import java.util.Set;
import java.util.HashSet;


public class MyHashMap<K, V> implements Map61B<K, V> {
	public MyHashMap() {}
	public MyHashMap(int initialSize) {}
	public MyHashMap(int initialSize, float loadFactor) {}
	public void clear() {}
	public boolean containsKey(K key){
		return true;
	}
	public V get (K key) {
		return null;
	}
	public int size() {
		return 0;
	}
	public void put(K key, V value) {}
	public V remove (K key) {
		return null;
	}
	public V remove (K key, V value) {
		return null;
	}
	public Set<K> keySet() {
		return new HashSet<K>();
	} 
}