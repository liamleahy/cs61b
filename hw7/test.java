import java.lang.Character;

public class test {
	public static void main(String[] args) {
		double i = Math.random() * 25 + 97;
		double j = Math.random() * 25 + 65;
		double k = Math.random() * 10 + 48;
		char a = (char) i;
		char b = (char) j;
		char c = (char) k;
		System.out.println(i + ": " + a);
		System.out.println(j + ": " + b);
		System.out.println(k + ": " + c);
		String tester = "tester";
		tester += Character.toString(a);
		System.out.println(tester);
	}
}