package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;

public class YearlyRecordTest{

	@Test
    public void testAll(){
        YearlyRecord yr = new YearlyRecord();
    	yr.put("zero", 1);
    	yr.put("this", 2);
    	yr.put("is", 19999);
    	yr.put("a", 3);
    	yr.put("test", 3);
    	System.out.println(yr.counts());
    	System.out.println(yr.words());
    	assertEquals(1, yr.count("zero"));
    	assertEquals(1, yr.rank("is"));    	
    	assertEquals(4, yr.rank("this"));
    	assertEquals(5, yr.size());
    	System.out.println(yr.rank("a"));
    	System.out.println(yr.rank("test"));
    }

    public static void main(String[] args){
    	jh61b.junit.textui.runClasses(YearlyRecordTest.class);
    }
}