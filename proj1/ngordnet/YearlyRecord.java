package ngordnet;
import java.util.TreeMap;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class YearlyRecord {  
    private int size = 0;
    private TreeMap<String, Integer> countMap = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> rankMap = new TreeMap<String, Integer>(); 
    private List<Ent> entList;
    private boolean cached = true;

    /** Object created for the use of conveniently
      * storing required data. 
      */
    private class Ent implements Comparable<Ent> {  
        private String word;
        private int count; 

        public String getWord() {
            return this.word;
        }

        public int getCount() {
            return this.count;
        }
        
        public Ent(String tword, int tcount) {  
            this.word = tword;
            this.count = tcount;
        }

        public int compareTo(Ent ent2) {  
            return this.getCount() - ent2.getCount();
        }
    }

    /** Creates a new empty YearlyRecord. */
    public YearlyRecord() {

    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {  
        countMap.putAll(otherCountMap);
        rankMap.putAll(otherCountMap);
        size += otherCountMap.size();
        cached = false;
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
        return countMap.get(word);
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {  
        countMap.put(word, count);
        cached = false;
        size += 1;
    }


    /** Returns rank of WORD. Most common word is rank 1. 
      * If two words have the same rank, break ties arbitrarily. 
      * No two words should have the same rank.
      */
    public int rank(String word) {  
        if (!cached) {  
            updateRankMap();
            cached = true;
        }
        return rankMap.get(word);
    }

    /** Returns the number of words recorded this year. */
    public int size() {  
        return this.size;
    }

    private void updateRankMap() {  
        rankMap = new TreeMap<String, Integer>();
        entList = new ArrayList<Ent>(); 
        for (String word : countMap.keySet()) {  
            entList.add(new Ent(word, countMap.get(word)));
        }
        Collections.sort(entList);
        int ranker = entList.size();
        for (Ent e : entList) {  
            rankMap.put(e.getWord(), ranker);
            ranker -= 1;
        }
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {  
        Collection<Number> n = new ArrayList<Number>();
        if (!cached) {  
            updateRankMap();
            cached = true;
        }
        for (Ent e : entList) {  
            n.add(e.getCount());
        }
        return n;
    }

    /** Returns all words in ascending order of count. */
    public Collection<String> words() {  
        Collection<String> n = new ArrayList<String>();
        if (!cached) {  
            updateRankMap();
            cached = true;
        }
        for (Ent e : entList) {  
            n.add(e.word);
        }
        return n;    
    }
}
