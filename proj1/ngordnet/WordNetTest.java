package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.LinkedHashSet;

public class WordNetTest{

	@Test
	public void testHyponyms(){
		WordNet wn = new WordNet("../wordnet/synsets11.txt", "../wordnet/hyponyms11.txt");
		Set<String> test = new LinkedHashSet<String>();
		test.add("augmentation");
		test.add("increase");
		test.add("leap");
		test.add("jump");
		System.out.println(test);
		assertEquals(test, wn.hyponyms("increase"));
	}
	
	@Test
	public void testIsNoun(){
		WordNet wn = new WordNet("../wordnet/synsets11.txt", "../wordnet/hyponyms11.txt");
		assertTrue(wn.isNoun("increase"));
		assertTrue(wn.isNoun("jump"));
	}

	@Test
	public void testNouns(){
		WordNet wn = new WordNet("../wordnet/synsets11.txt", "../wordnet/hyponyms11.txt");
		Set<String> test = new LinkedHashSet<String>();
		String s = "augmentation nasal_decongestant change action actifed antihistamine increase descent parachuting leap demotion jump";
		String[] sa = s.split(" ");
		for (String p : sa){
			test.add(p);
		}
		assertEquals(test, wn.nouns());
	}

	public static void main(String[] args){
        jh61b.junit.textui.runClasses(WordNetTest.class);
    }

}