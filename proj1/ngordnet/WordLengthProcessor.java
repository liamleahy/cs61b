package ngordnet;

public class WordLengthProcessor implements YearlyRecordProcessor{

	public WordLengthProcessor(){

	}

	public double process(YearlyRecord yr){
		int runningTotal = 0;
		for (String word : yr.words()){
			runningTotal += yr.count(word) * word.length();
		}
		int absTotal = 0;
		for (Number i : yr.counts()){
			absTotal += (Integer) i;
		}
		return (runningTotal / absTotal);
	}





}