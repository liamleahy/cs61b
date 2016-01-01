package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class NGramMapTest {

	@Test
    public void testAll() {
    	NGramMap ngm = new NGramMap("../p1data/ngrams/words_that_start_with_q.csv", 
                                    "../p1data/ngrams/total_counts.csv");
        System.out.println(ngm.countInYear("quantity", 1736)); // should print 139
        YearlyRecord yr = ngm.getRecord(1736);
        System.out.println(yr.count("quantity")); // should print 139

        TimeSeries<Integer> countHistory = ngm.countHistory("quantity");
        System.out.println(countHistory.get(1736)); // should print 139

        TimeSeries<Long> totalCountHistory = ngm.totalCountHistory();
        /* In 1736, there were 8049773 recorded words. Note we are not counting
         * distinct words, but rather the total number of words printed that year. */
        System.out.println( totalCountHistory.get(1736)); // should print 8049773

        TimeSeries<Double> weightHistory = ngm.weightHistory("quantity");
        assertEquals(1.7267E-5, weightHistory.get(1736), 1e-5);  // should print roughly 1.7267E-5
    
        System.out.println((double) countHistory.get(1736) 
                           / (double) totalCountHistory.get(1736)); 

        ArrayList<String> words = new ArrayList<String>();
        words.add("quantity");
        words.add("quality");        

        TimeSeries<Double> sum = ngm.summedWeightHistory(words);
        assertEquals(3.875E-5, sum.get(1736), 1e-5); // should print roughly 3.875E-5

    }

    public static void main(String[] args) {
    	jh61b.junit.textui.runClasses(NGramMapTest.class);
    }
}