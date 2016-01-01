import org.junit.Test;
import org.junit.Rule;	
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

public class TestAutocomplete {

	@Test
	public void testLevenshteinDistance () {
		assertEquals(Autocomplete.levenshteinDistance("aaa", "baa"), 1);
	}

	@Test
	public void testBasic() {
		String[] terms = {"test", "other", "something", "else", "danny", "test1", "test2"};
		double[] weights = {1.0, 2.0, 1.5, 4.0, 0.5, 1.0, 25.0};
		Autocomplete a = new Autocomplete(terms, weights);
		assertEquals(1.0, a.weightOf("test"), 1e-5);
		assertEquals("test2", a.topMatch("test"));
	}

	// @Test
	// public void testSpellCheck() {
	// 	String[] terms = {"test", "other", "something", "else", "danny", "test1", "test2","tast"};
	// 	double[] weights = {1.0, 2.0, 1.5, 4.0, 0.5, 1.0, 25.0, 100.0};
	// 	Autocomplete a = new Autocomplete(terms, weights);
	// 	assertEquals("tast", a.spellCheck("test", 1, 1));
	// }

	public static void main(String[] args) {
		jh61b.junit.textui.runClasses(TestAutocomplete.class);
	}
}