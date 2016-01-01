import org.junit.Test;
import org.junit.Rule;	
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.ArrayList;

public class testtst {

	@Test
	public void testBasic() {
		TST tst = new TST();
		tst.insert("test1", 1.0);
		tst.insert("test2", 2.0);
		tst.insert("aaa", 0.5);
		tst.insert("zzz", 99.99);
		assertEquals(tst.valueOf("zzz"), 99.99, 1e-5);
		assertEquals(tst.valueOf("test1"), 1.0, 1e-5);
		assertEquals(tst.valueOf("aaa"), 0.5, 1e-5);
		assertEquals(tst.valueOf("test2"), 2.0, 1e-5);
		assertEquals(tst.valueOf("somethingelse"), 0.0, 1e-5);
		TST.Node node = tst.get(tst.root, "test", 0);
		assertEquals(node.c, 't');
		assertEquals(node.prefix, "test");
		assertEquals(node.max, 2.0, 1e-5);
		assertEquals(node.mid.right.prefix, "test2");
		assertEquals(node.mid.prefix, "test1");
		assertEquals(node.mid.val, 1.0, 1e-5);

	}

	@Test
	public void nextTest() {
		TST tst = new TST();
		tst.insert("test1", 1.0);
		tst.insert("test2", 2.0);
		tst.insert("aaa", 30.0);
		tst.insert("test3", 234.0);
		tst.insert("zzz", 300.0);
		// System.out.println(tst.root.left.max);
		// System.out.println(tst.root.max);		
		// System.out.println(tst.root.right.max);		
		// System.out.println(tst.root.mid.max);
		// TST.Node temp = tst.get(tst.root,"test", 0);
		// System.out.println(temp.c);		
	}

	// @Test
	// public void testComparator() {
	// 	TST tst = new TST();
	// 	tst.insert("test1", 1.0);
	// 	tst.insert("test2", 2.0);
	// 	tst.insert("aaa", 30.0);
	// 	tst.insert("test3", 234.0);
	// 	tst.insert("zzz", 300.0);
	// 	PriorityQueue<TST.Node> pq = new PriorityQueue<TST.Node>(4, new TST.Node.NodeComparator());
	// 	pq.add(tst.root);
	// 	pq.add(tst.root.left);
	// 	TST.Node max = pq.poll();
	// 	System.out.println(max.max);
	// 	System.out.println(max.c);
	// 	System.out.println(max.mid.c);
	// 	System.out.println(max.left.c);
	// 	System.out.println(max.right.c);		
	// 	TST.Node max2 = pq.poll();
	// 	System.out.println(max2.max);
	// 	System.out.println(max2.c);
	// 	for (String s : tst.keysThatMatch("tast1")) {
	// 		System.out.println(s);
	// 	}
	// 	Queue<String> q = new Queue<String>();
	// }

	@Test 
	public void testTopMatches() {
		TST tst = new TST();
		tst.insert("smog", 5.0);
		tst.insert("buck", 10.0);
		tst.insert("sad", 12.0);
		tst.insert("spite", 20.0);
		tst.insert("spit", 15.0);
		tst.insert("spy", 7.0);
		Iterator<String> iter = tst.topMatches("s", 3).iterator();
		ArrayList<String> check = new ArrayList<String>();
		while (iter.hasNext()) {
			String temp = iter.next();
			check.add(temp);
			System.out.println(temp);
		}
		assertTrue(check.contains("spite"));
	}

	@Test 
	public void testTopMatches1() {
		TST tst = new TST();
		tst.insert("aaa", 5.0);
		tst.insert("abb", 10.0);
		tst.insert("zsfd", 12.0);
		tst.insert("aa;ljksdf", 20.0);
		tst.insert("ljakjfg", 15.0);
		tst.insert("lkasjdf", 7.0);
		Iterator<String> iter = tst.topMatches("", 3).iterator();
		ArrayList<String> check = new ArrayList<String>();
		while (iter.hasNext()) {
			String temp = iter.next();
			check.add(temp);
		}
		assertTrue(check.contains("aa;ljksdf"));
	}


	@Test 
	public void testTopMatches2() {
		TST tst = new TST();
		tst.insert("smog", 5.0);
		tst.insert("buck", 10.0);
		tst.insert("sad", 12.0);
		tst.insert("spite", 20.0);
		tst.insert("spit", 15.0);
		tst.insert("spy", 7.0);
		Iterator<String> iter = tst.topMatches("b", 3).iterator();
		ArrayList<String> check = new ArrayList<String>();
		while (iter.hasNext()) {
			String temp = iter.next();
			check.add(temp);
			System.out.println(temp);
		}
		assertTrue(check.contains("buck"));
		assertTrue(check.size() == 1);
	}

	@Test 
	public void testTopMatches3() {
		TST tst = new TST();
		tst.insert(";alsdfjk", 5.0);
		tst.insert("ljkdfgafa", 10.0);
		tst.insert(";ljkasdf;a", 12.0);
		tst.insert("wlkjertl;qj34taf", 20.0);
		tst.insert("weawglea;kjrsv;isrelg", 15.0);
		tst.insert("a;dfa;sldfkja;lkjtrs", 7.0);
		Iterator<String> iter = tst.topMatches("w", 2).iterator();
		ArrayList<String> check = new ArrayList<String>();
		while (iter.hasNext()) {
			String temp = iter.next();
			check.add(temp);
		}
		assertTrue(check.contains("wlkjertl;qj34taf"));
		assertTrue(check.contains("weawglea;kjrsv;isrelg"));
	}

	@Test 
	public void testTopMatches4() {
		TST tst = new TST();
		tst.insert(";alsdfjk", 5.0);
		tst.insert("ljkdfgafa", 10.0);
		tst.insert(";ljkasdf;a", 12.0);
		tst.insert("lkjertl;qj34taf", 20.0);
		tst.insert("weawglea;kjrsv;isrelg", 15.0);
		tst.insert("a;dfa;sldfkja;lkjtrs", 7.0);
		Iterator<String> iter = tst.topMatches("", 4).iterator();
		ArrayList<String> check = new ArrayList<String>();
		while (iter.hasNext()) {
			String temp = iter.next();
			check.add(temp);
		}
		assertTrue(check.contains("lkjertl;qj34taf"));
		assertTrue(check.contains("weawglea;kjrsv;isrelg"));
		assertTrue(check.contains(";ljkasdf;a"));
		assertTrue(check.contains("ljkdfgafa"));
	}

	public static void main(String[] args) {
		jh61b.junit.textui.runClasses(testtst.class); 
	}
}
	