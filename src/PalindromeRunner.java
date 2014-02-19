package src;

public class PalindromeRunner {

	private static String[][] values = { 
		{"A23", "16"},
		{"A345", "12"},
		{"196", "10"},
		{"ABDCEF", "16"}
	};
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**PalindromeFinder pf = new PalindromeFinder(values[0][0], convertThatBase(values[0][1]));
		System.out.println(pf.findPalindrome());
		//*/
		/**
		for(int i = 0; i < values.length; i++){
			PalindromeFinder pf = new PalindromeFinder(values[i][0], convertThatBase(values[i][1]));
			System.out.println(pf.getAnswer(pf.findPalindrome()));
		}//*/
		String num = args[0];
		int base = convertThatBase(args[1]);
		PalindromeFinder pf = new PalindromeFinder(num, base);
		System.out.println(pf.getAnswer(pf.findPalindrome()));
	}
	
	/**
	 * Converts the String representation of the base into an int
	 * @param base the String representation of the base
	 * @return the int value of the base
	 */
	private static int convertThatBase(String base){
		int result = Integer.parseInt(base);
		return result;
	}

}
