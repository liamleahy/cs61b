import org.junit.Test;
import org.junit.Rule;	
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

/** Tests proj3 shtuff.
  * @author Liam Leahy 
  */

public class TrieTest {
	@Rule
    public ExpectedException thrown= ExpectedException.none();

	@Test 
	public void testTrieFind() {
		Trie t = new Trie();
	    t.insert("hello");
	    t.insert("hey");
	    t.insert("goodbye");
	    assertTrue(t.find("hello", true));
	    assertTrue(t.find("goodbye", true));
	    assertTrue(t.find("hell", false));
	    assertFalse(t.find("hell", true));
	    assertFalse(t.find("good", true));
	    assertFalse(t.find("bye", false));
	    assertFalse(t.find("heyy", false));
	    assertFalse(t.find("h", true));
	}

	@Test 
	public void testPartialWord() {
		Trie t = new Trie();
		t.insert("josh");
		t.insert("joshhugs");
		t.insert("joshhubs");
		assertFalse(t.find("joshshrugs", false));
		assertTrue(t.find("josh", true));
		assertTrue(t.find("josh", false));
		assertTrue(t.find("jos", false));
		assertTrue(t.find("joshhugs", true));
		assertTrue(t.find("joshhubs", true));

	}

	@Test
	public void testNumTrie() {
		TST tst = new TST();
		tst.insert("hello", 1.1);
		tst.insert("goodbye", 3.7);
		tst.insert("goodday", 20.1);
		tst.insert("meh", 282349237);
		assertEquals(tst.valueOf("hello"), 1.1, 1e-4);
		assertEquals(tst.valueOf("goodbye"), 3.7, 1e-4);
		assertEquals(tst.valueOf("goodday"), 20.1, 1e-4);
		assertEquals(tst.valueOf("meh"), 282349237, 1e-4);
		assertEquals(tst.valueOf("something else"), 0.0, 1e-4);
	}

	@Test
	public void testTrieExceptions() {
		Trie t = new Trie();
		thrown.expect(IllegalArgumentException.class);
		t.insert("");
		t.insert(null);
	}

	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TrieTest.class);  
	}
}