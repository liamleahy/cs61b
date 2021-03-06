/* Radix.java */

package radix;

/**
 * Sorts is a class that contains an implementation of radix sort.
 * @author
 */
public class Sorts {


    /**
     *  Sorts an array of int keys according to the values of <b>one</b>
     *  of the base-16 digits of each key. Returns a <b>NEW</b> array and
     *  does not modify the input array.
     *  
     *  @param key is an array of ints.  Assume no key is negative.
     *  @param whichDigit is a number in 0...7 specifying which base-16 digit
     *    is the sort key. 0 indicates the least significant digit which
     *    7 indicates the most significant digit
     *  @return an array of type int, having the same length as "keys"
     *    and containing the same keys sorted according to the chosen digit.
     **/
    public static int[] countingSort(int[] keys, int whichDigit) {
        int[] ret = new int[keys.length];
        int[] counts = new int[16]; 
        int[] starts = new int[16];
        for (int i : keys) {
            counts [(i >> (whichDigit * 4)) & 15] += 1;
        }
        for (int j = 0; j < 16; j ++) {
            if (j == 0) {
                starts[j] = 0;
            } else {
                starts[j] = starts[j-1] + counts[j-1];
            }
        }
        for (int k : keys) {
            int num = (k >> (whichDigit * 4)) & 15;
            ret[starts[num]] = k;
            starts[num] += 1;
        }
        return ret;
    }

    /**
     *  radixSort() sorts an array of int keys (using all 32 bits
     *  of each key to determine the ordering). Returns a <b>NEW</b> array
     *  and does not modify the input array
     *  @param key is an array of ints.  Assume no key is negative.
     *  @return an array of type int, having the same length as "keys"
     *    and containing the same keys in sorted order.
     **/
    public static int[] radixSort(int[] keys) {
        int digit = 0;
        int [] ret = new int[keys.length];
        System.arraycopy(keys, 0, ret, 0, keys.length);
        while (digit < 8) {
            ret = countingSort(ret, digit);
            digit += 1;
        }
        return ret;
    }

}
