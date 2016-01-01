import java.util.List;
import java.util.AbstractList;

public class ArrayList61B<T> extends AbstractList<T> {
	private int size;
	private T[] arr;

	public ArrayList61B(int init) {
		if (init <= 0) {
			throw new IllegalArgumentException("nope");
		}
		arr = (T[]) new Object[init];
		size = 0;
	}

	public ArrayList61B() {
		this(1);
	}

	public T get(int i) {
		if (i < 0 || i >= size) {
			throw new IllegalArgumentException("nop");
		}
		return arr[i];
	}

	public boolean add(T item) {
		size += 1;
		arr[size - 1] = item;
		if (size >= arr.length) {
			T[] temp = arr;
			arr = (T[]) new Object[size * 2];
			for (int i = 0; i < size; i += 1) {
				arr[i] = temp[i];
			}
		}
		return true;
	}

	public int size() {
		return size;
	}
}
