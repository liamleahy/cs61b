import edu.princeton.cs.introcs.In;
import edu.princeton.cs.algs4.Digraph;
import java.util.HashMap;
import java.lang.*;
import java.util.Map.Entry;
import java.util.Collection;
import java.util.ArrayList;
import ngordnet.Plotter;
import java.util.Comparator;
import ngordnet.NGramMap;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import ngordnet.YearlyRecord;

public class test{
	
	// public  Comparator<String> strComp = new Comparator<String>(){
	// 	@Override public int compare(String word1, String word2){
	// 		return compFunc(word1) - compFunc(word2);
	// 	}
	// };
	// public SortedMap<String, Integer> map = new TreeMap<String, Integer>(strComp);

	// private int compFunc(String word){
	// 	return map.get(word);
	// }
	
	// public void doStuff(){
	// 	map.put("something", 3);
	// 	map.put("sometheing else", 2);
	// 	map.put("alksdjfadf", 1);
	// 	for (Entry e : map.entrySet()){
	// 		System.out.println(e.getKey());
	// 	}		
	// }

	public static void main(String[] args){
		// In a = new In("./wordnet/synsets11.txt");
		// String[] s1 = a.readAllLines();
		// for (String s : s1){
		// 	System.out.println(s);
		// }
		// In b = new In("./wordnet/hyponyms11.txt");
		// String[] s2 = b.readAllLines();
		// for (String s5 : s2){
		// 	System.out.println(s5);
		// }
		// Digraph d = new Digraph(s1.length);
		// HashMap<String, String> hm = new HashMap<String, String>();
		// for (String s : s2){
		// 	String[] p = s.split(",");
		// 	for (String q : p){
		// 		if (Integer.parseInt(p[0]) != Integer.parseInt(q)){
		// 			d.addEdge(Integer.parseInt(p[0]), Integer.parseInt(q));
		// 		}
		// 	}
		// }
		// System.out.println(d);
		// String z = "hello";
		// String[] ret = z.split(" ");
		// System.out.println(ret[0]);
		// String tester = "I am              separated     by  random          spaces.";
		// String newTester = tester.replaceAll("\\s+"," ");
		// String[] testrAr = newTester.split(" ");
		// for (String intermedi : testrAr){
		// 	System.out.println(intermedi);
		// }
		// long l = 0;
		// int i = 22;
		// l += i; 
		// System.out.println(l);
		// HashMap<Integer, YearlyRecord> yrhm = new HashMap<Integer, YearlyRecord>();
		// YearlyRecord nyr = new YearlyRecord();
		// nyr.put("Sausage", 21);
		// yrhm.put(1994, (YearlyRecord)nyr);
		// System.out.println(yrhm.get(1994));
		// for (Entry e : yrhm.entrySet()){
		// 	System.out.println(e.getValue());
		// 	System.out.println(((YearlyRecord)e.getValue()).count("Sausage"));
		// }
		// Integer zerg = 1;
		// System.out.println(zerg instanceof Integer);
		// Integer zerg1 = (Integer) null;
		// System.out.println(zerg1);
		// System.out.println(zerg1 instanceof Integer);
		// System.out.println(java.lang.Integer.MAX_VALUE);
		// Collection<Integer> testerino = new ArrayList<Integer>();
		// testerino.add(new Integer(5));
		// testerino.add(new Integer(10));
		// testerino.add(new Integer(5));
		// for (Integer testerinoi : testerino){
		// 	System.out.println(testerinoi);
		// }
		// NGramMap ngm = new NGramMap("./p1data/ngrams/words_that_start_with_q.csv", "./p1data/ngrams/total_counts.csv");
		// String[] wordsToUse ={"quality", "quantity"};
		// //Plotter.plotAllWords(ngm, wordsToUse, 0, 2005);
		YearlyRecord yr = new YearlyRecord();
		yr.put("test", 10);
		yr.put("test1", 11);
		System.out.println(yr.count("test2"));
		Integer i = null;
		assert i != null;
		System.out.println(i);
	}

}