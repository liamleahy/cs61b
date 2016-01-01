import java.util.Scanner;

public class Test {
	public static void main(String[] args) {
		System.out.println("Please input string: ");
		Scanner reader = new Scanner(System.in);
		String c = reader.next();
		System.out.println(c);
		while (c.equals("c")) {
			c = reader.next();
		}
	}
}