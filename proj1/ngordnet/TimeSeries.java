package ngordnet;
import java.util.TreeMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {
    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        super(ts);
    }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR. 
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        super(ts.subMap(startYear, true, endYear, true));
    }

    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
        super();
    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        Collection<Number> ret = new ArrayList<Number>();
        /**Inspired by Zed's stackoverflow post here: 
          *http://stackoverflow.com/questions/1318980/how-to-iterate-over-a-treemap
          */
        for (Map.Entry<Integer, T> entry : this.entrySet()) {
            Integer key = entry.getKey();
            ret.add(key);
        }
        return ret;
    }

    /** Returns all data for this time series. 
      * Must be in the same order as years(). */
    public Collection<Number> data() {
        Collection<Number> ret = new ArrayList<Number>();
        for (Map.Entry<Integer, T> entry : this.entrySet()) {
            T value = entry.getValue();
            ret.add(value);
        }
        return ret;
    }

    /** Returns the sum of this time series with the given ts. The result is a 
      * a Double time series (for simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> tSum = new TimeSeries<Double>();
        if (ts.data().isEmpty()) {
            for (Number n : this.years()) {
                tSum.put((Integer) n, this.get(n).doubleValue());
            }
            return tSum;
        }
        if (this.data().isEmpty()) {
            for (Number n : ts.years()) {
                tSum.put((Integer) n, ts.get(n).doubleValue());
            }
            return tSum;
        }
        ArrayList<Integer> cache = new ArrayList<Integer>();
        for (Number n : ts.years()) {
            tSum.put((Integer) n, ts.get(n).doubleValue());
        }
        for (Number n : this.years()) {
            if (tSum.containsKey(n)) {
                tSum.put((Integer) n, tSum.get(n).doubleValue() + this.get(n).doubleValue());
            } else {
                tSum.put((Integer) n, this.get(n).doubleValue());
            }
        }
        return tSum;
    }

    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> tDiv = new TimeSeries<Double>();
        if (!this.years().equals(ts.years())) {
            throw new IllegalArgumentException("Incompatible TimeSeries");
        }
        for (Map.Entry<Integer, T> entry : this.entrySet()) {
            Integer d = entry.getKey();
            tDiv.put(d, this.get(d).doubleValue() / ts.get(entry.getKey()).doubleValue());
        }    
        return tDiv;
    }
}
