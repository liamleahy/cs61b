package ngordnet;
import edu.princeton.cs.introcs.In;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NGramMap {
    private TreeMap<Integer, YearlyRecord> yrrec = new TreeMap<Integer, YearlyRecord>();
    private TimeSeries<Long> ts = new TimeSeries<Long>();
    private int maxYear = 0;
    private TreeSet<Integer> yearCache = new TreeSet<Integer>();

    /** Constructs an fNGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        In words = new In(wordsFilename);
        In counts = new In(countsFilename);
        String[] wordsAr = words.readAllLines();
        String[] countsAr = counts.readAllLines();
        for (String wordString : wordsAr) {
            String temp = wordString.replaceAll("\\s+", " ");
            String[] in = temp.split(" ");
            int year = Integer.parseInt(in[1]);
            if (yrrec.containsKey(year)) {
                YearlyRecord zerg = yrrec.remove(year);
                zerg.put(in[0], Integer.parseInt(in[2]));
                yrrec.put(year, zerg);
            } else {
                YearlyRecord adder = new YearlyRecord();
                adder.put(in[0], Integer.parseInt(in[2]));
                yrrec.put(year, adder);
            }
        }
        for (String countsString : countsAr) {
            String[] in1 = countsString.split(",");            
            ts.put(Integer.parseInt(in1[0]), Long.parseLong(in1[1]));
        }
        for (Number n : ts.years()) {
            if ((Integer) n > maxYear) {
                maxYear = (Integer) n;
            }
        }
    }

    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {
        if (yrrec.get(year).words().contains(word)) {
            return yrrec.get(year).count(word);
        }
        return 0;
    }

    /** Returns a defensive copy of the YearlyRecord of YEAR. */
    public YearlyRecord getRecord(int year) {
        HashMap<String, Integer> tempHm = new HashMap<String, Integer>();
        for (String word: yrrec.get(year).words()) {
            tempHm.put(word, yrrec.get(year).count(word));
        }
        return new YearlyRecord(tempHm);
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> ret = new TimeSeries<Integer>();
        for (Map.Entry e : this.yrrec.entrySet()) {
            if ((((YearlyRecord) e.getValue()).words().contains(word))) {
                if (((Integer) e.getKey() >= startYear) && ((Integer) e.getKey() <= endYear)) {
                    ret.put((Integer) e.getKey(), ((YearlyRecord) e.getValue()).count(word));
                }
            }
        }
        return ret;
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        return countHistory(word, 0, maxYear);
    }
 
    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        return ts;
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> cH = countHistory(word, startYear, endYear);
        TimeSeries<Double> ret = new TimeSeries<Double>();
        for (Number n : cH.years()) {
            Integer d = (Integer) n;
            ret.put(d, (double) cH.get(d) / (double) totalCountHistory().get(d));
        }
        return ret;
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word) {
        return weightHistory(word, 0, maxYear);
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. If a word does not exist, ignore it rather
      * than throwing an exception. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, int startYear,
                                                                                int endYear) {
        TimeSeries<Double> ret = new TimeSeries<Double>();
        for (String s : words) {
            TimeSeries<Double> intermediary = weightHistory(s, startYear, endYear);
            ret = ret.plus(intermediary);
        }
        return ret;
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, 0, maxYear);
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
                                                    YearlyRecordProcessor yrp) {
        TimeSeries<Double> ret = new TimeSeries<Double>();
        for (Map.Entry e : yrrec.entrySet()) {
            if ((Integer) e.getKey() <= endYear && (Integer) e.getKey() >= startYear) {
                ret.put((Integer) e.getKey(), yrp.process(((YearlyRecord) e.getValue())));
            }
        }
        return ret;
    }
}




