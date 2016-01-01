import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;

public class RemoveDirectories {

	public static void main(String[] args) {
		File cur = new File(".");
        ArrayList<File> dir = new ArrayList<File>(Arrays.asList(cur.listFiles()));
        for (File f : dir) {
        	if (f.isDirectory()) {
        		f.delete();
        	}
        }
	}
}