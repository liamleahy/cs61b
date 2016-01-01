public class LeapYear {
	public static void main(String[] args) {
		is_Leap_Year();
	}
	public static void is_Leap_Year() {
		int year = 2000;
	    if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
	        System.out.println(Integer.toString(year) + " is a leap year.");            
	    }
	    else {
	     	System.out.println(Integer.toString(year) + " is not a leap year.");
	     } 	
	}

}