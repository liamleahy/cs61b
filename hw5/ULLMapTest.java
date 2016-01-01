import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;

/** ULLMapTest. You should write additional tests.
 *  @author Josh Hug
 */

public class ULLMapTest {
    @Test
    public void testBasic() {
        ULLMap<String, String> um = new ULLMap<String, String>();
        um.put("Gracias", "Dios Basado");
        assertEquals(um.get("Gracias"), "Dios Basado");
    }

    @Test
    public void testOther() {
        ULLMap<String, String> um = new ULLMap<String, String>();
        um.put("Test", "Test");
        assertTrue(um.containsKey("Test"));
    }
    
    @Test
    public void testIterator() {
        ULLMap<Integer, String> um = new ULLMap<Integer, String>();
        um.put(0, "zero");
        um.put(1, "one");
        um.put(2, "two");
        assertEquals(um.get(0), "zero");
        Iterator<Integer> iter = um.iterator();
        int i = 2;
        assertEquals(i, (int) iter.next());;
    }

    @Test 
    public void testInvert() {
        ULLMap<Integer, String> um = new ULLMap<Integer, String>();
        um.put(0, "zero");
        um.put(1, "one");
        um.put(2, "two");
        ULLMap<String, Integer> umo = ULLMap.invert(um);
        assertEquals((int)umo.get("one"), 1);
    }
    

    /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(ULLMapTest.class);
    }
} 