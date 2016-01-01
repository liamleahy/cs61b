package radix;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.Math;

public class TestRadix {

	@Test
	public void runTest() {
		int[] input = new int[1000];
		for (int i = 0; i < 1000; i += 1) {
			input[i] = (int)(Math.random() * 10000);
		}
		int[] output = Sorts.radixSort(input);
		for (int j = 1; j < 1000; j += 1) {
			assertTrue(output[j] >= output[j-1]);
		}
	}

    public static void main(String[] args) {
    jh61b.junit.textui.runClasses(TestRadix.class);      
	}

}