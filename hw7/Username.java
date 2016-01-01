<<<<<<< HEAD
import java.lang.Character;

=======
>>>>>>> 2ec6dbd42bbc675508ed28ab542c2bda11f4c243
public class Username {

    // Potentially useless note: (int) '0' == 48, (int) 'a' == 97

    // Instance Variables (remember, they should be private!)
    // YOUR CODE HERE
<<<<<<< HEAD
    private String username;

    public Username() {
        // YOUR CODE HERE
        double if_two = Math.random();
        String fin = "";
        int j;

        if (if_two <= .5) {
            j = 1;
        } else {
            j = 2;
        }
        for (int i = 0; i <= j; i += 1) {
            fin += randAlphaNumeric();
        }
        this.username = fin;
        
    }

    private String randAlphaNumeric() {
        double i = Math.random() * 25 + 97;
        double j = Math.random() * 25 + 65;
        double k = Math.random() * 10 + 48;
        char a = (char) i;
        char b = (char) j;
        char c = (char) k;

        double random = Math.random() * 9;

        if (random <= 3) {
            return Character.toString(a);
        } else if (random <= 6) {
            return Character.toString(b);
        } else {
            return Character.toString(c);
        }
=======

    public Username() {
        // YOUR CODE HERE
>>>>>>> 2ec6dbd42bbc675508ed28ab542c2bda11f4c243
    }

    public Username(String reqName) {
        // YOUR CODE HERE
<<<<<<< HEAD
        if (reqName.equals(null)) {
            throw new NullPointerException("Requested username is null!");
        }
        if (reqName.length() == 2 || reqName.length() == 3) {
            for (int i = 0; i < reqName.length(); i += 1) {
                if (!((Character.isLetter(reqName.charAt(i))) || Character.isDigit(reqName.charAt(i)))) {
                    throw new IllegalArgumentException("Not alphanumeric");
                }
            }
            this.username = reqName;
        }
        throw new IllegalArgumentException("Wrong length");
=======
>>>>>>> 2ec6dbd42bbc675508ed28ab542c2bda11f4c243
    }

    @Override
    public boolean equals(Object o) {
<<<<<<< HEAD
        return this.username.equals(((Username) o).username);
=======
        // YOUR CODE HERE
        return false;
>>>>>>> 2ec6dbd42bbc675508ed28ab542c2bda11f4c243
    }

    @Override
    public int hashCode() { 
<<<<<<< HEAD
        return username.hashCode();
    }

    public static void main(String[] args) {
        Username test = new Username();
        System.out.println(test.username);
=======
        // YOUR CODE HERE
        return 0;
    }

    public static void main(String[] args) {
        // You can put some simple testing here.
>>>>>>> 2ec6dbd42bbc675508ed28ab542c2bda11f4c243
    }
}