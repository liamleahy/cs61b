package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.Collection;

public class TimeSeriesTest{

	@Test
	public void testYearsAndData(){
        TimeSeries<Double> ts = new TimeSeries<Double>();
		ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        System.out.println(ts.years());
        System.out.println(ts.data());
	}

	@Test
	public void testPlus(){
		TimeSeries<Double> ts = new TimeSeries<Double>();
		ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        TimeSeries<Double> ts1 = new TimeSeries<Double>();
        ts1.put(1992, 5.0);
        ts1.put(1991, 3.0);
        ts1.put(1993, -2.0);
        ts1.put(1994, -20.0);
        TimeSeries<Double> ts2 = ts.plus(ts1);
        assertEquals(ts2.get(1992).doubleValue(), 8.6,1e-5);
        assertEquals(ts2.get(1991).doubleValue(), 3.0,1e-5);
        assertEquals(ts2.get(1993).doubleValue(), 7.2,1e-5);
        assertEquals(ts2.get(1994).doubleValue(), -4.8,1e-5);
        TimeSeries<Double> t3 = new TimeSeries<Double>();
        TimeSeries<Double> t4 = t3.plus(ts2);
        TimeSeries<Double> t5 = ts2.plus(t3);
        System.out.println(t5.equals(ts2));
        System.out.println(t4.equals(ts2));	
        TimeSeries<Double> tN = new TimeSeries<Double>(ts2);
        assertEquals(ts2.get(1992), tN.get(1992));

        TimeSeries<Double> tst = new TimeSeries<Double>(ts1);
        TimeSeries<Double> tst1 = new TimeSeries<Double>();
        tst1.put(2000, 5.0);
        tst1.put(2001, 6.7);
        TimeSeries<Double> tst2 = tst.plus(tst1);
        assertEquals(tst2.get(2000), 5.0, 1e-10);
        assertEquals(tst2.get(1992), 5.0, 1e-10);

	}

	@Test
	public void testDiv(){
		TimeSeries<Double> ts = new TimeSeries<Double>();
		ts.put(1992, 2.5);
        ts.put(1993, 8.0);
        ts.put(1994, -2.0);
        TimeSeries<Double> ts1 = new TimeSeries<Double>();
        ts1.put(1992, 5.0);
        ts1.put(1993, -2.0);
        ts1.put(1994, -20.0);
        TimeSeries<Double> tQuotient = ts1.dividedBy(ts);
        assertEquals(tQuotient.get(1992).doubleValue(), 2.0, 1e-5);
        assertEquals(tQuotient.get(1993).doubleValue(), -.25, 1e-5);
        assertEquals(tQuotient.get(1994).doubleValue(), 10.0, 1e-5);
	}

	public static void main(String[] args){
        jh61b.junit.textui.runClasses(TimeSeriesTest.class);
    }

}