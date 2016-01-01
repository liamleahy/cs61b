package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;
import ngordnet.NGramMap;
import ngordnet.WordNet;
import ngordnet.Plotter;
import java.util.Set;
import ngordnet.WordLengthProcessor;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author [yournamehere mcjones]
 */
public class NgordnetUI{
    public static void main(String[] args){
        In in = new In("../ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");
        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile +
                           ", and " + hyponymFile + ".");
        WordNet wn = new WordNet(synsetFile, hyponymFile);
        NGramMap ngm = new NGramMap(wordFile, countFile);
        int startDate = 0;
        int endDate = 3000;
        while (true){
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            try{
              switch (command){
                case "quit": 
                    return;
                case "help":
                    In otherIn = new In("help.txt");
                    String helpStr = otherIn.readAll();
                    System.out.println(helpStr);
                    break;  
                case "range": 
                    startDate = Integer.parseInt(tokens[0]); 
                    endDate = Integer.parseInt(tokens[1]);
                    System.out.println("Start date: " + startDate);
                    System.out.println("End date: " + endDate);
                    break;
                  case "count":
                    System.out.println(ngm.countInYear(tokens[0], Integer.parseInt(tokens[1])));
                    break;
                  case "hyponyms":
                    System.out.println(wn.hyponyms(tokens[0]));
                    break;
                  case "history":
                    Plotter.plotAllWords(ngm, tokens, startDate, endDate);
                    break;
                  case "hypohist":
                    Plotter.plotCategoryWeights(ngm, wn, tokens, startDate, endDate);
                    break;
                  case "wordlength":
                    Plotter.plotProcessedHistory(ngm, startDate, endDate, new WordLengthProcessor());
                    break;
                  case "zipf":
                    Plotter.plotZipfsLaw(ngm, Integer.parseInt(tokens[0]));
                    break;
                  case "dates":
                    System.out.println("Start date: " + startDate);
                    System.out.println("End date: " + endDate);
                    break;
                default:
                    System.out.println("Invalid command.");  
                    break;
            }
          }
          catch (Exception e){
            System.out.println("Error: " + e);
          }
        }
    }
} 
