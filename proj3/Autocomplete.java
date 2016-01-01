import java.util.LinkedList;
import java.util.HashSet;

/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 * @author Liam Leahy, The Great and Awesome and..... yeah you got the rest.
 */
public class Autocomplete {
    TST tst;
    /**
     * Initializes required data structures from parallel arrays.
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {
        int termsSize = terms.length;
        int weightsSize = weights.length;
        HashSet<Double> dcache = new HashSet<Double>(weightsSize);
        HashSet<String> scache = new HashSet<String>(termsSize);
        if (termsSize != weightsSize) {
            throw new IllegalArgumentException();
        }
        tst = new TST();
        for (int i = 0; i < termsSize; i += 1) {
            if (scache.contains(terms[i])) {
                throw new IllegalArgumentException();
            } else {
                scache.add(terms[i]);
                dcache.add(weights[i]);
            }
            if (weights[i] <= 0.0) {
                throw new IllegalArgumentException();
            }
            tst.insert(terms[i], weights[i]);
        }
    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     * @param term the String being evaluated.
     * @return The weight of given term in the TST.
     */
    public double weightOf(String term) {
        return tst.valueOf(term);
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {
        return tst.topMatch(prefix);
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     * @param prefix is the String prefix.
     * @param k number of Strings that you want in the Iterable.
     * @return Iterable<String> of the topMatches for the prefix.
     */
    public Iterable<String> topMatches(String prefix, int k) {
        if (k <= 0) {
            throw new IllegalArgumentException();
        }
        if (k == 1) {
            LinkedList<String> lst = new LinkedList<String>();
            lst.add(topMatch(prefix));
            return lst;
        }
        return tst.topMatches(prefix, k);
    }

    /**
     * Returns the highest weighted matches within k edit distance of the word.
     * If the word is in the dictionary, then return an empty list.
     * @param word The word to spell-check
     * @param dist Maximum edit distance to search
     * @param k    Number of results to return 
     * @return Iterable in descending weight order of the matches
     */
    public Iterable<String> spellCheck(String word, int dist, int k) {
        return new HashSet<String>();  
    }

    /** Helper for lenenshteinDistance
      * @param a one int to find min of. 
      * @param b one int to find min of. 
      * @param c one int to find min of. 
      * @return the minimum of three ints.
      */
    private static int minimum(int a, int b, int c) {                            
        return Math.min(Math.min(a, b), c);                                      
    }     

    /** Helper method straight from wikipedia: 
      * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
      * Used to dtermine the edit distance between two strings.
      * @param s the first string.
      * @param t the second string.
      * @return the distance between s and t.
      */
    public static int levenshteinDistance(String s, String t) {
                                                                       
        int[][] distance = new int[s.length() + 1][t.length() + 1];        
 
        for (int i = 0; i <= s.length(); i++) {                              
            distance[i][0] = i;  
        }                                                
        for (int j = 1; j <= t.length(); j++) {                            
            distance[0][j] = j;                                                  
        }
        for (int i = 1; i <= s.length(); i++) {                              
            for (int j = 1; j <= t.length(); j++) {                         
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,                                  
                        distance[i][j - 1] + 1,                                  
                        distance[i - 1][j - 1] + ((s.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1));
            }
        }
 
        return distance[s.length()][t.length()];                           
    }                                                                            

    /**
     * Test client. Reads the data from the file, 
     * then repeatedly reads autocomplete queries from standard input and 
     * prints out the top k matching terms.
     * @param args takes the name of an input file and an integer k as command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);

        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
            }
        }
    }
}
