import java.util.Scanner;
import java.util.Set;

/** This thing called AlphabetSort as requested
  * @author Liam Leahy, The Great and Wonderful and Funny and Handsome and Totally Awesome 
  */
public class AlphabetSort {
    public static final int SIZE = 256;
    /** It makes a new trie and then takes in an alphabet and file.
        And then it does what it's supposed to do from there.
        @param args The arguments for the mainMethod.
    */
    public static void main(String[] args) {
        Trie t = new Trie();
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNext()) {
            throw new IllegalArgumentException();
        }
        String alphabet = scanner.next();
        if (!scanner.hasNext()) {
            throw new IllegalArgumentException();
        }
        if (hasRepeats(alphabet)) {
            throw new IllegalArgumentException();
        }
        while (scanner.hasNext()) {
            String s = scanner.next();
            t.insert(s);
        }
        printAll(t, alphabet);
    }

    /** Determines if s has repeats
      * @param s String that is being checked for repeats.
      * @return boolean Evaluation of repeatingness of s.
      */
    public static boolean hasRepeats(String s) {
        char[] cs = new char[SIZE];
        for (int i = 0; i < s.length(); i += 1) {
            char temp = s.charAt(i);
            if (cs[(int) temp] != '\u0000') {
                return true;
            }
            cs[(int) temp] = temp;
        }
        return false;
    }

    /** Prints every word in the trie. 
      * @param alphabet String that represents the specified alphabet. 
      * @param t is the trie in question.
      */
    public static void printAll(Trie t, String alphabet) {
        if (alphabet.length() == 0) {
            throw new IllegalArgumentException();
        }
        printTraversal(alphabet, "", t.getNode());
    }

    /** Is the thing that actually does the printing.
      * @param alphabet String[] that represents the alphabet in order.
      * @param soFar String that is to be added to the words when reached.
      * @param n is the Node that we're considering.
      */
    public static void printTraversal(String alphabet, String soFar, Trie.Node n) {
        if (n.end()) {
            System.out.println(soFar);
        }
        Set<Character> s = n.getCurr().keySet();
        char[] alphabetAr = alphabet.toCharArray();
        for (char c : alphabetAr) {
            if (s.contains(c)) {
                printTraversal(alphabet, soFar + c, n.getCurr().get(c));
            }
        }

    }
}
