import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.TreeMap;


public class test {
	public static void main(String[] args) {
		PriorityQueue<TST.Wrapper> tm = new PriorityQueue<TST.Wrapper>(3, new TST.Wrapper.compr());
		tm.add(new TST.Wrapper(1.0, "test1"));
		tm.add(new TST.Wrapper(2.0, "test2"));
		tm.add(new TST.Wrapper(3.0, "test3"));
		tm.add(new TST.Wrapper(0.1, "test4"));
		for (TST.Wrapper w : tm) {
			System.out.println(w.val);
		}
	}

	public static int round(double d) {
		if (d % 1 < .5) {
			return (int) d;
		}
		return (int) (d + 1);
	}
}