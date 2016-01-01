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
import java.util.Scanner;
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
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Collection;
import java.util.NavigableSet;

public class Gitlet extends Help {
    private static final String CURRENT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        switch (args[0]) {
            case "init": 
                init();
                break;
            case "add":
                add(args[1]);
                break;
            case "commit":
                commit(args[1]);
                break;
            case "log":
                log(getCurrentCommit());
                break;
            case "global-log":
                globalLog();
                break;
            case "find":
                find(args[1]);
                break;
            case "remove":
                remove(args[1]);
                break;
            case "status":
                status();
                break;
            case "checkout":
                if (args.length == 2) {
                    if(checkoutBranch(args[1])) {
                        break;
                    } else if (checkOutFile(args[1])) {
                    	break;
                    }
                } else if (args.length == 3) {
                	checkoutWithID(Integer.parseInt(args[1]), args[2]);
                }
                break;
            case "branch":
            	branch(args[1]);
            	break;
            case "rm-branch":
            	removeBranch(args[1]);
            	break;
            case "reset":
            	reset(args[1]);
            	break;
           	case "merge":
           		merge(args[1]);
           		break;
            case "rebase":
                rebase(args[1]);
                break;
            case "i-rebase":
                iRebase(args[1]);
                break;
            default:
                break;
        }
    }

    public static void iRebase(String branchName) {
        HashMap<Integer, String> heads = readInObject(".gitlet/heads.ser");
        HashMap<String, HashMap<Integer, String>> branches = readInObject(".gitlet/branches.ser");
        String currentBranchName = readInObject(".gitlet/currentBranchName.ser");
        if (currentBranchName.equals(branchName)) {
            System.out.println("Cannot rebase a branch onto itself.");
            return;
        }
        if (!branches.keySet().contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        HashMap<Integer, String> branch = branches.get(branchName);
        Commit commonAncestor = commonAncestor(branchName);
        Commit otherBranchCommit = readInObject(branches.get(branchName).get(0));
        Commit currentCommit = readInObject(heads.get(0));
        if (commonAncestor.equals(currentCommit)) {
            String temporary = heads.get(currentCommit.getID());
            heads.put(0, ".gitlet/" + Integer.toString(otherBranchCommit.getID()) + ".ser");
            heads.put(currentCommit.getID(), ".gitlet/" + Integer.toString(currentCommit.getID()) + ".ser");
            writeObjectIn(".gitlet/heads.ser", heads);
            changeEnvironment(otherBranchCommit);
            return;
        }
        Commit toAdd = interactiveGetCommitChain(currentCommit, commonAncestor, otherBranchCommit, currentCommit);
        heads.put(getMaxID() + 1, heads.get(0));
        heads.put(0, ".gitlet/" + Integer.toString(toAdd.getID()) + ".ser");
        writeObjectIn(".gitlet/heads.ser", heads);
    }

    public static void rebase(String branchName) {
        HashMap<Integer, String> heads = readInObject(".gitlet/heads.ser");
        HashMap<String, HashMap<Integer, String>> branches = readInObject(".gitlet/branches.ser");
        String currentBranchName = readInObject(".gitlet/currentBranchName.ser");
        if (currentBranchName.equals(branchName)) {
            System.out.println("Cannot rebase a branch onto itself.");
            return;
        }
        if (!branches.keySet().contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        HashMap<Integer, String> branch = branches.get(branchName);
        Commit commonAncestor = commonAncestor(branchName);
        Commit otherBranchCommit = readInObject(branches.get(branchName).get(0));
        Commit currentCommit = readInObject(heads.get(0));
        if (commonAncestor.equals(currentCommit)) {
            String temporary = heads.get(currentCommit.getID());
            heads.put(0, ".gitlet/" + Integer.toString(otherBranchCommit.getID()) + ".ser");
            heads.put(currentCommit.getID(), ".gitlet/" + Integer.toString(currentCommit.getID()) + ".ser");
            writeObjectIn(".gitlet/heads.ser", heads);
            changeEnvironment(otherBranchCommit);
            return;
        }
        Commit toAdd = getCommitChain(currentCommit, commonAncestor, otherBranchCommit);
        heads.put(getMaxID() + 1, heads.get(0));
        heads.put(0, ".gitlet/" + Integer.toString(toAdd.getID()) + ".ser");
        writeObjectIn(".gitlet/heads.ser", heads);
    }

    public static void merge(String branchName) {
    	HashMap<Integer, String> heads = readInObject(".gitlet/heads.ser");
    	HashMap<String, HashMap<Integer, String>> branches = readInObject(".gitlet/branches.ser");
   		String currentBranchName = readInObject(".gitlet/currentBranchName.ser");
   		if (currentBranchName.equals(branchName)) {
   			System.out.println("Cannot merge a branch with itself.");
   			return;
   		}
   		if (!branches.keySet().contains(branchName)) {
   			System.out.println("A branch with that name does not exist.");
   			return;
   		}
   		Commit commonAncestor = commonAncestor(branchName);
   		Commit otherBranchCommit = readInObject(branches.get(branchName).get(0));
   		Commit currentCommit = readInObject(heads.get(0));
   		HashMap<String, String> modifiedFiles = nonConflictionFinder(otherBranchCommit, commonAncestor, currentCommit);
   		for (String file : modifiedFiles.keySet()) {
   			Commit temp = readInObject(modifiedFiles.get(file));
   			byte[] encoded = temp.getFiles().get(file);
   			replaceFile(new File(file), encoded, file);
   		}
   		HashMap<String, String> conflictions = conflictionFinder(otherBranchCommit, commonAncestor, currentCommit);
   		for (String s : conflictions.keySet()) {
   			Commit temp1 = readInObject(conflictions.get(s));
   			byte[] encoded1 = temp1.getFiles().get(s);
   			addFile(encoded1, s + ".conflicted");
   		}
    }

    public static void reset(String commitID) {
    	Commit toChange = readInObject(".gitlet/" + commitID + ".ser");
    	if (toChange == null) {
    		System.out.println("No commit with that id exists.");
    	} else {
    		updateHeads(".gitlet/" + commitID + ".ser");
			changeEnvironment(toChange);
		}
    }

    public static void branch(String branchName) {
    	HashMap<Integer, String> heads = readInObject(".gitlet/heads.ser");
    	HashMap<String, HashMap<Integer, String>> branches = readInObject(".gitlet/branches.ser");
    	String currentBranchName = readInObject(".gitlet/currentBranchName.ser");
    	branches.put(currentBranchName, heads);
    	HashMap<Integer, String> newHeads = new HashMap<Integer, String>();
    	newHeads.putAll(heads);
    	writeObjectIn(".gitlet/heads.ser", newHeads);
    	writeObjectIn(".gitlet/branches.ser", branches);
    	writeObjectIn(".gitlet/currentBranchName.ser", branchName);
    }

   	public static void checkoutWithID(int ID, String fileName) {
   		TreeMap<Integer, String> commits = readInObject(".gitlet/commits.ser");
   		if (commits.keySet().contains(new Integer(ID))) {
   			Commit c = readInObject(".gitlet/" + Integer.toString(ID) + ".ser");
   			if (c == null) {
   				System.out.println("No commit with that id exists.");
   			}
   			if (c.containsFile(fileName)) {
   				replaceFile(new File(fileName), c.getFile(fileName), fileName);
   				return;
   			}
   		}
   		System.out.println("File does not exist in that commit.");
   	}

    public static boolean checkOutFile(String fileName) {
    	File f = new File(fileName);
    	Commit c = getCurrentCommit();
    	HashMap<String, String> otherFiles = c.getInherited();
    	if (c.getFiles().keySet().contains(fileName)) {
    		replaceFile(f, c.getFiles().get(fileName), fileName);
    		return true;
    	} else if (otherFiles.keySet().contains(fileName)) {
    		Commit otherCommit = readInObject(otherFiles.get(fileName));
    		replaceFile(f, otherCommit.getFiles().get(fileName), fileName);
    		return true;
    	}
    	System.out.println("File does not exist in the most recent commit, or no such branch exists.");
    	return false;
    }

    public static boolean checkoutBranch(String branchName) {
	   	String currentBranchName  = readInObject(".gitlet/currentBranchName.ser");
	   	if (branchName.equals(currentBranchName)) {
	   		System.out.println("No need to checkout the current branch.");
	   		return true;
	   	}
    	HashMap<String, HashMap<Integer, String>> branches = readInObject(".gitlet/branches.ser");
    	if (branches.keySet().contains(branchName)) {
    		HashMap<Integer, String> currentBranch = readInObject(".gitlet/heads.ser");
	    	branches.put(currentBranchName, currentBranch);
	    	HashMap<Integer, String> newCurrentBranch = branches.get(branchName);
	    	branches.remove(branchName);
	    	writeObjectIn(".gitlet/heads.ser", newCurrentBranch); 
	    	writeObjectIn(".gitlet/currentBranchName.ser", branchName);
	    	writeObjectIn(".gitlet/branches.ser", branches);
    		Commit c = readInObject(newCurrentBranch.get(0));
    		changeEnvironment(c);
    		return true;
    	}
    	return false;
    }

    public static void status() {
    	System.out.println("=== Branches ===");
    	HashMap<String, HashMap<Integer, String>> branches = readInObject(".gitlet/branches.ser");
    	String currentBranchName = readInObject(".gitlet/currentBranchName.ser");
    	System.out.println("*" + currentBranchName);
    	for (String s : branches.keySet()) {
    		System.out.println(s);
    	}
    	System.out.println();
    	System.out.println("=== Staged Files ===");
    	HashSet<String> adds = readInObject(".gitlet/add.ser");
    	for (String g : adds) {
    		System.out.println(g);
    	}
    	System.out.println();
    	System.out.println("=== Files Marked for Removal ===");
    	HashSet<String> trash = readInObject(".gitlet/trash.ser");
    	for (String h : trash) {
    		System.out.println(h);
    	}
    }

    public static void remove(String fileName) {
    	HashSet<String> trash = readInObject(".gitlet/trash.ser");
    	HashSet<String> adds = readInObject(".gitlet/add.ser");
    	if (adds.contains(fileName)) {
    		adds.remove(fileName);
    		writeObjectIn(".gitlet/add.ser", adds);
    		return;
    	}
    	Commit current = getCurrentCommit();
    	if (current.getFiles().containsKey(fileName)) {
	    	trash.add(fileName);
    		writeObjectIn(".gitlet/trash.ser", trash);
    		return;    		
    	}
    	System.out.println("No reason to remove the file.");
    }

    public static void find(String commitMessage) {
    	HashMap<String, ArrayList<Integer>> commitMessages = readInObject(".gitlet/commitMessages.ser");
    	if (commitMessages.containsKey(commitMessage)) {
    		ArrayList<Integer> temp = commitMessages.get(commitMessage);
    		for (Integer i : temp) {
    			System.out.println(i);
    		}
    	}
    }

    public static void removeBranch(String branchName) {
    	HashMap<String, HashMap<Integer, String>> branches = readInObject(".gitlet/branches.ser");
    	String currentBranchName = readInObject(".gitlet/currentBranchName.ser");
    	if (currentBranchName.equals(branchName)) {
    		System.out.println("Cannot remove the current branch.");
    		return;
    	}
    	if (!branches.keySet().contains(branchName)) {
    		System.out.println("A branch with that name does not exist.");
    	} else {
    		branches.remove(branchName);
    		writeObjectIn(".gitlet/branches.ser", branches);
    	}
    }

    public static void log(Commit start) {
        if (start == null) {
            return;
        }
        System.out.println("====");
        System.out.println("Commit " + Integer.toString(start.getID()) + ".");
        System.out.println(timeToString(start.getTime()));
        System.out.println(start.getCommitMessage() + "\n");
        log(start.getParent());
    }

    public static void globalLog() {
    	int max = getMaxID();
    	while(max >= 0) {
            Commit c = readInObject(".gitlet/" + Integer.toString(max) + ".ser");
            System.out.println("====");
            System.out.println("Commit " + Integer.toString(c.getID()) + ".");
            System.out.println(timeToString(c.getTime()));
            System.out.println(c.getCommitMessage() + "\n");
            max -= 1;
    	}
    }

    public static void add(String fileName) {
    	File f = new File(fileName);
    	Commit current = getCurrentCommit();
    	if (f.exists()) {
    		if (current.containsFile(fileName)) {
    			if (f.lastModified() - current.getTime() <= 0) {
    				System.out.println("File has not been modified since the last commit.");
    				return;
    			}
    		}
    		HashSet<String> adds = readInObject(".gitlet/add.ser");
    		adds.add(fileName);
    		writeObjectIn(".gitlet/add.ser", adds);
    	} else {
    		System.out.println("File does not exist.");
    	}
    }

    public static void commit(String commitMessage) {
    	if (commitMessage == null) {
    		System.out.println("Please enter a commit message.");
    		return;
    	}
        HashSet<String> adds = readInObject(".gitlet/add.ser");
        if (adds.isEmpty()) {
        	System.out.println("No changes added to the commit.");
        	return;
        }
    	HashMap<Integer, String> heads = readInObject(".gitlet/heads.ser");
    	String currentCommit = heads.get(0);
    	Commit current = readInObject(currentCommit);
    	TreeMap<Integer, String> commits = readInObject(".gitlet/commits.ser");
        long millis = System.currentTimeMillis();
        HashMap<String, ArrayList<Integer>> commitMessages = readInObject(".gitlet/commitMessages.ser");
        HashMap<String, byte[]> filesToAdd = new HashMap<String, byte[]>();
        HashMap<String, byte[]> olds = current.getFiles();
        HashMap<String, String> filesToInherit = current.getInherited();
        for (String newFile : olds.keySet()) {
        	filesToInherit.put(newFile, ".gitlet/" + Integer.toString(current.getID()) + ".ser");
        }
        HashSet<String> trash = readInObject(".gitlet/trash.ser");
        for (String s : adds) {
        	filesToAdd.put(s, getBytes(s));
        }
        for (String str : trash) {
        	filesToAdd.remove(str);
        }
    	Commit newCommit = new Commit(commitMessage, current, getMaxID() + 1, millis, filesToAdd, filesToInherit);
    	String commitFileName = ".gitlet/" + Integer.toString(newCommit.getID()) + ".ser";
    	heads.put(0, commitFileName);
    	heads.put(new Integer(Collections.max(heads.keySet()) + 1), currentCommit);
    	commits.put(newCommit.getID(), commitFileName);
    	ArrayList<Integer> ids = new ArrayList<Integer>();
    	if (commitMessages.containsKey(commitMessage)) {
    		ids = commitMessages.get(commitMessage);
    	}
    	ids.add(newCommit.getID());    	
    	commitMessages.put(commitMessage, ids);
    	writeObjectIn(".gitlet/maxID.ser", newCommit.getID());
    	writeObjectIn(".gitlet/commitMessages.ser", commitMessages);
    	writeObjectIn(".gitlet/commits.ser", commits);
    	writeObjectIn(commitFileName, newCommit);
    	writeObjectIn(".gitlet/heads.ser", heads);
    	writeObjectIn(".gitlet/add.ser", new HashSet<String>());
    	writeObjectIn(".gitlet/trash.ser", new HashSet<String>());
    }

    public static void init() {
    	Commit initCommit = null;
        File initialization = new File(".gitlet");
        if (initialization.mkdir()) { 
            long millis = System.currentTimeMillis();
            initCommit = new Commit("initial commit", millis);
            writeObjectIn(".gitlet/0.ser", initCommit);
            HashMap<Integer, String> heads = new HashMap<Integer, String>();
            heads.put(0, ".gitlet/0.ser");
            writeObjectIn(".gitlet/heads.ser", heads);
            HashMap<String, HashMap<Integer, String>> branches = new HashMap<String, HashMap<Integer, String>>();
            writeObjectIn(".gitlet/branches.ser", branches);
            HashSet<String> adds = new HashSet<String>();
            writeObjectIn(".gitlet/add.ser", adds);
            HashSet<String> trash = new HashSet<String>();
            writeObjectIn(".gitlet/trash.ser", trash);
            TreeMap<Integer, String> commits = new TreeMap<Integer, String>();
            commits.put(0, ".gitlet/0.ser");
            writeObjectIn(".gitlet/commits.ser", commits);
            HashMap<String, ArrayList<Integer>> commitMessages = new HashMap<String, ArrayList<Integer>>();
            ArrayList<Integer> temp = new ArrayList<Integer>();
            temp.add(0);
            int maxID = 0;
            writeObjectIn(".gitlet/maxID.ser", maxID);
            commitMessages.put("initial commit", temp);
            writeObjectIn(".gitlet/commitMessages.ser", commitMessages);
            String currentBranchName = "master";
            writeObjectIn(".gitlet/currentBranchName.ser", currentBranchName);
        }
        else {
            System.out.println("A gitlet version control system already exists in the current directory.");
        }
    }
}

class Help {
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
        TreeMap<Integer, String> commits = readInObject(".gitlet/commits.ser");
        long millis = System.currentTimeMillis();
        if (start == null) {
            return start;
        }
        else if (end.equals(start.getParent())) {
            Commit temp1 = new Commit(start.getCommitMessage(), attachmentPoint, getMaxID() + 1, millis, start.getFiles(), start.getInherited());
            commits.put(temp1.getID(), ".gitlet/" + Integer.toString(temp1.getID()) + ".ser");
            writeObjectIn(".gitlet/commits.ser", commits);
            writeObjectIn(".gitlet/maxID.ser", getMaxID() + 1);
            writeObjectIn(".gitlet/" + Integer.toString(temp1.getID()) + ".ser", temp1);
            return temp1;
        }
        Commit temp = new Commit(start.getCommitMessage(), getCommitChain(start.getParent(), end, attachmentPoint), getMaxID() + 1, millis, start.getFiles(), start.getInherited());
        commits.put(temp.getID(), ".gitlet/" + Integer.toString(temp.getID()) + ".ser");
        writeObjectIn(".gitlet/maxID.ser", getMaxID() + 1);
        writeObjectIn(".gitlet/" + Integer.toString(temp.getID()) + ".ser", temp);
        writeObjectIn(".gitlet/commits.ser", commits);
        return temp;
    }

    public static Commit interactiveGetCommitChain(Commit start, Commit end, Commit attachmentPoint, Commit actualStart) {
        TreeMap<Integer, String> commits = readInObject(".gitlet/commits.ser");
        long millis = System.currentTimeMillis();
        if (start == null) {
            return start;
        }
        else if (end.equals(start.getParent()) || start.equals(actualStart)) {
            System.out.println("Currently replaying: ");
            System.out.println("====");
            System.out.println("Commit " + Integer.toString(start.getID()) + ".");
            System.out.println(timeToString(start.getTime()));
            System.out.println(start.getCommitMessage() + "\n");
            System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
            Scanner q = new Scanner(System.in);
            String w = q.next();
            while (!(w.equals("s") || w.equals("c") || w.equals("m"))) {
                System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
                w = q.next();
            }
            while (!w.equals("m") && !w.equals("c")) {
                System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
                w = q.next();
            }
            if (w.equals("m")) {
                System.out.println("Please enter a new message for this commit.");
                w = q.next();
                Commit temp1 = new Commit(w, attachmentPoint, getMaxID() + 1, millis, start.getFiles(), start.getInherited());
                commits.put(temp1.getID(), ".gitlet/" + Integer.toString(temp1.getID()) + ".ser");
                writeObjectIn(".gitlet/commits.ser", commits);
                writeObjectIn(".gitlet/maxID.ser", getMaxID() + 1);
                writeObjectIn(".gitlet/" + Integer.toString(temp1.getID()) + ".ser", temp1);
                return temp1;                
            } else if (w.equals("c")) {
                Commit temp1 = new Commit(start.getCommitMessage(), attachmentPoint, getMaxID() + 1, millis, start.getFiles(), start.getInherited());
                commits.put(temp1.getID(), ".gitlet/" + Integer.toString(temp1.getID()) + ".ser");
                writeObjectIn(".gitlet/commits.ser", commits);
                writeObjectIn(".gitlet/maxID.ser", getMaxID() + 1);
                writeObjectIn(".gitlet/" + Integer.toString(temp1.getID()) + ".ser", temp1);
                return temp1;
            }
        }
        System.out.println("Currently replaying: ");
        System.out.println("====");
        System.out.println("Commit " + Integer.toString(start.getID()) + ".");
        System.out.println(timeToString(start.getTime()));
        System.out.println(start.getCommitMessage() + "\n");
        System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        while (!(s.equals("s") || s.equals("c") || s.equals("m"))) {
            System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
            s = scanner.next();
        }
        if (s.equals("c")) {
            Commit temp = new Commit(start.getCommitMessage(), interactiveGetCommitChain(start.getParent(), end, attachmentPoint, actualStart), getMaxID() + 1, millis, start.getFiles(), start.getInherited());
            commits.put(temp.getID(), ".gitlet/" + Integer.toString(temp.getID()) + ".ser");
            writeObjectIn(".gitlet/maxID.ser", getMaxID() + 1);
            writeObjectIn(".gitlet/" + Integer.toString(temp.getID()) + ".ser", temp);
            writeObjectIn(".gitlet/commits.ser", commits);
            return temp;                
        } else if (s.equals("s")) {
            return interactiveGetCommitChain(start.getParent(), end, attachmentPoint, actualStart);
        } else if (s.equals("m")) {
            System.out.println("Please enter a new message for this commit.");
            Scanner scan = new Scanner(System.in);
            String p = scan.next();
            Commit temp = new Commit(p, interactiveGetCommitChain(start.getParent(), end, attachmentPoint, actualStart), getMaxID() + 1, millis, start.getFiles(), start.getInherited());
            commits.put(temp.getID(), ".gitlet/" + Integer.toString(temp.getID()) + ".ser");
            writeObjectIn(".gitlet/maxID.ser", getMaxID() + 1);
            writeObjectIn(".gitlet/" + Integer.toString(temp.getID()) + ".ser", temp);
            writeObjectIn(".gitlet/commits.ser", commits);
            return temp;  
        } else {
            return interactiveGetCommitChain(start, end, attachmentPoint, actualStart);
        }
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

    public static void rebaseEnvironment(Commit current, Commit other) {
        HashMap<String, String> currentFiles = getAllFiles(current);
        HashMap<String, String> otherFiles = getAllFiles(other);
        for (String file : otherFiles.keySet()) {
            if (!currentFiles.keySet().contains(file)) {
                Commit temp = readInObject(otherFiles.get(file));
                addFile(temp.getFile(file), file);
            }
        }
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

class Commit extends Help implements Serializable, Comparable<Commit> {
    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private static final long serialVersionUID = 42L;
    private String commitMessage;
    private HashMap<String, byte[]> files; //maps fileName --> byte[] of corresponding file
    private Commit parent;
    private int id;
    private long time;
    private HashMap<String, String> inheritedFiles; //maps fileName to commitName that contains the byte[] of this file

    public Commit(String commitMessage, Commit parent, Integer id, Long time, HashMap<String, byte[]> files, HashMap<String, String> inheritedFiles) {
        this.commitMessage = commitMessage;
        this.parent = parent;
        this.id = id;
        this.time = time;
        this.files = files;
        this.inheritedFiles = inheritedFiles;
    }

    @Override
    public boolean equals(Object other) {
        return this.getID() == ((Commit)other).getID();
    }

    @Override
    public int hashCode() {
        return ((Integer)id).hashCode();
    }

    public HashMap<String, String> getInherited() {
        return inheritedFiles;
    }

    @Override
    public int compareTo(Commit other) {
        return this.getID() - other.getID();
    }

    public Commit(String commitMessage, Long time) {
        this(commitMessage, null, 0, time, new HashMap<String, byte[]>(), new HashMap<String, String>());
    }

    public int getID() {
        return id;
    }

    public boolean containsFile(String fileName) {
        return files.keySet().contains(fileName);
    }

    public byte[] getFile(String fileName) {
        if (files.keySet().contains(fileName)) {
            return files.get(fileName);
        }
        Commit other = readInObject(inheritedFiles.get(fileName));
        return other.getFiles().get(fileName);
    }

    public long getTime() {
        return time;
    }

    public Commit getParent() {
        return parent;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public HashMap<String, byte[]> getFiles() {
        return files;
    }
}












