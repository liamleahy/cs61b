package proj2;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.channels.*;
import java.lang.Comparable;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Collections;

public class Help {
    private static final String CURRENT_DIR = System.getProperty("user.dir");


	public static <T> void writeObjectIn(String fileName, T object) {
		File fileToAdd = new File(fileName);
		if (fileToAdd.exists()) {
			fileToAdd.delete();
		}
		try {
			ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(fileToAdd));
			objectOut.writeObject(object);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

    @SuppressWarnings("unchecked")
    public static <T> T readInObject(String fileName) {
    	File adds = new File(fileName);
    	T ret = null;
    	try {
    		ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(adds));
    		ret = (T)objectIn.readObject();
    	} catch (IOException e) {
    		System.out.println(e);
    	} catch (ClassNotFoundException e) {
    		System.out.println(e);
    	}
    	return ret;
    }

    public static String timeToString(Long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formatter.format(time);
    }

    /** Returns a mapping of fileName --> commitName for all files that are changed in
   	  * both branchCommit and currentCommit.
    */
    public static HashMap<String, String> conflictionFinder(Commit branchCommit, Commit ancestorCommit, Commit currentCommit) {
    	HashMap<String, String> branchFiles = getAllFiles(branchCommit);
    	HashMap<String, String> currentFiles = getAllFiles(currentCommit);
    	HashMap<String, String> ancestorFiles = getAllFiles(ancestorCommit);
    	Set<String> branchKeys = branchFiles.keySet();
    	Set<String> currentKeys = currentFiles.keySet();
    	Set<String> ancestorKeys = ancestorFiles.keySet();
    	HashMap<String, String> ret = new HashMap<String, String>();
    	for (String s : branchKeys) {
    		if (!branchFiles.get(s).equals(ancestorFiles.get(s)) && !currentFiles.get(s).equals(ancestorFiles.get(s))) {
    			ret.put(s, branchFiles.get(s));
    		}
    	}
    	return ret;	
    }

    /** Returns a mapping of the fileName --> commitName for all files that have been modified
      * (from ancestorCommit) in branchCommit but not in currentCommit.
      */
    public static HashMap<String, String> nonConflictionFinder(Commit branchCommit, Commit ancestorCommit, Commit currentCommit) {
    	HashMap<String, String> branchFiles = getAllFiles(branchCommit);
    	HashMap<String, String> currentFiles = getAllFiles(currentCommit);
    	HashMap<String, String> ancestorFiles = getAllFiles(ancestorCommit);
    	Set<String> branchKeys = branchFiles.keySet();
    	Set<String> currentKeys = currentFiles.keySet();
    	Set<String> ancestorKeys = ancestorFiles.keySet();
    	HashMap<String, String> ret = new HashMap<String, String>();
    	for (String s : branchKeys) {
    		if (!branchFiles.get(s).equals(ancestorFiles.get(s)) && currentFiles.get(s).equals(ancestorFiles.get(s))) {
    			ret.put(s, branchFiles.get(s));
    		}
    	}
    	return ret;	
    }

    public static HashMap<String, String> getAllFiles(Commit c) {
    	HashMap<String, String> inherited = c.getInherited();
    	HashMap<String, byte[]> files = c.getFiles();
    	HashMap<String, String> ret = new HashMap<String, String>();
    	ret.putAll(inherited);
    	for (String p : files.keySet()) {
    		ret.put(p, ".gitlet/" + Integer.toString(c.getID()) + ".ser");
    	}
    	return ret;
    }

    public static Commit getCommitChain(Commit start, Commit end, Commit attachmentPoint) {
        if (end == start) {
            Commit temp1 = new Commit(start.getCommitMessage(), attachmentPoint, getMaxID() + 1, start.getTime(), start.getFiles(), start.getInherited());
            writeObjectIn(".gitlet/maxID.ser", getMaxID() + 1);
            writeObjectIn(".gitlet/" + Integer.toString(temp1.getID()) + ".ser", temp1);
            return temp1;
        } else if (start == null) {
            return null;
        }
        Commit temp = new Commit(start.getCommitMessage(), getCommitChain(start.getParent(), end, attachmentPoint), getMaxID() + 1, start.getTime(), start.getFiles(), start.getInherited());
        writeObjectIn(".gitlet/maxID.ser", getMaxID() + 1);
        writeObjectIn(".gitlet/" + Integer.toString(temp.getID()) + ".ser", temp);
        return temp;
    }

    public static Commit commonAncestor(String branchName) {
    	HashMap<String, HashMap<Integer, String>> branches = readInObject(".gitlet/branches.ser");
    	HashMap<Integer, String> branch = branches.get(branchName);
    	HashMap<Integer, String> heads = readInObject(".gitlet/heads.ser");
    	Commit branchCommit = readInObject(branch.get(0));
    	Commit thisCommit = readInObject(heads.get(0));
        HashSet<Integer> inSet = new HashSet<Integer>();
        while (branchCommit != null) {
            inSet.add(branchCommit.getID());
            branchCommit = branchCommit.getParent();
        }
    	while (thisCommit != null) {
            if (inSet.contains(thisCommit.getID())) {
                Commit ret = readInObject(".gitlet/" + Integer.toString(thisCommit.getID()) + ".ser");
                return ret;
            }
            thisCommit = thisCommit.getParent();
        }
        Commit defaultRet = readInObject(".gitlet/0.ser");
        return defaultRet;
    }

    public static void changeEnvironment(Commit other) {
        File cur = new File(".");
        ArrayList<File> dir = new ArrayList<File>(Arrays.asList(cur.listFiles()));
        HashMap<String, byte[]> newFiles = other.getFiles();
        HashMap<String, String> inheritedFiles = other.getInherited();
        for (File g : dir) {
            if (newFiles.keySet().contains(g.getName()) && (g.lastModified() - other.getTime() >= 0 && !g.isDirectory())) {
                byte[] file = newFiles.get(g.getName());
                replaceFile(g, file, g.getName());
                newFiles.remove(file);
            } else if (inheritedFiles.keySet().contains(g.getName())) {
            	Commit temp = readInObject(inheritedFiles.get(g.getName()));
            	if (g.lastModified() - temp.getTime() >= 0 && !g.isDirectory()) {
	            	HashMap<String, byte[]> tempFiles = temp.getFiles();
	            	byte[] filer = tempFiles.get(g.getName());
	            	replaceFile(g, filer, g.getName());            		
            	}
            } else if (!g.isDirectory()) {
            	g.delete();
            }
        }
        for (String s : newFiles.keySet()) {
            addFile(newFiles.get(s), s);
        }
    }

    public static void addFile(byte[] add, String name) {
        replaceFile(new File (""), add, name);
    } 

    public static void replaceFile(File oldFile, byte[] encoded, String newName) {
    	if (encoded == null) {
    		return;
    	}
        try {
            oldFile.delete();
            FileOutputStream fos = new FileOutputStream(newName);
            fos.write(encoded);
            fos.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static Commit getCurrentCommit() {
    	HashMap<Integer, String> heads = readInObject(".gitlet/heads.ser");
    	Commit ret = readInObject(heads.get(0));
    	return ret;
    }

    public static int getMaxID() {
    	int maxID = readInObject(".gitlet/maxID.ser");
    	return maxID;
    }

    public static void updateHeads(String newCommit) {
    	HashMap<Integer, String> heads = readInObject(".gitlet/heads.ser");
    	String currentCommit = heads.get(0);
    	heads.put(0, newCommit);
    	heads.put(getMaxID() + 1, currentCommit);
    	writeObjectIn(".gitlet/heads.ser", heads);
    }

    public static byte[] getBytes(String fileName) {
    	byte[] ret = null;
    	try {
    		ret = Files.readAllBytes(Paths.get(fileName));
    	} catch (IOException e) {
    		System.out.println(e);
    	}
    	return ret;
    }
}





