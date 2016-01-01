public class Test {
	public static void main(String[] args) {
		int i = 177;
		int j = (i >> 4);
		System.out.println(j);
		System.out.println(j & 15); 
		int[] k = new int[16];
		int counter = 0;
		for (int in : k) {
			counter += 1;
		}
		System.out.println(counter);
	}
}