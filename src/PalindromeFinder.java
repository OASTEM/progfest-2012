package src;

import java.util.ArrayList;

public class PalindromeFinder {

	private final String theNum;
	private final int theBase;
	public String answer, noneResult;
	public int lastNum;
	public ArrayList<String> answers;
	
	/**
	 * Constructs a Palindrome Finding Object
	 * @param num number to start with, in String form
	 * @param base the base num is in
	 */
	public PalindromeFinder(String num, int base){
		theNum = num;
		theBase = base;
	}
	
	/**public PalindromeFinder(ArrayList<String> list){
		theNum = "-1";
		theBase = -1;
		answers = new ArrayList<String>();
		parseInput(list);
	}
	
	/**
	 * Converts a base letter to a number value (limited to base 16)
	 * @param letter The letter to convert
	 * @return the value of the letter as a number value
	 */
	private long convertBaseLetterToNum(String letter){
		switch(letter){
		case "A" : return 10L;
		case "B" : return 11L;
		case "C" : return 12L;
		case "D" : return 13L;
		case "E" : return 14L;
		case "F" : return 15L;
		default : return -1L; // we should not be going here today (or ever)
		}
	}
	
	/**
	 * Converts a number to a base letter representation (limited to base 16)
	 * @param num number to convert
	 * @return either the number if it is within 0-9, or a base letter representation
	 */
	private String convertNumToBaseLetter(int num){
		switch(num){
		case 10 : return "A";
		case 11 : return "B";
		case 12 : return "C";
		case 13 : return "D";
		case 14 : return "E";
		case 15 : return "F";
		default : return num + "";
		}
	}
	
	/**
	 * Converts a String representation of a number into a number (base 10)
	 * @param num the number in String, any base up to 16
	 * @return the base 10 value of the number
	 */
	private long convertStringToNum(String num){
		try {
			long result = Long.parseLong(num);
			return result;
		}
		catch(NumberFormatException e){
			long result = convertBaseLetterToNum(num);
			return result;
		}
	}
	
	/**
	 * Converts a base 10 number to the base initalized with this object
	 * @param decValue the base 10 value to convert
	 * @return the String representation of the number in the corresponding base (theBase)
	 */
	public String convertToBase(long decValue){  
        String result = "";
        int maxPower = 0;
        if(theBase == 10){
            return result+decValue;
        }
        boolean notOver;
        do{
            long powValue = (long) Math.pow(theBase, maxPower);
            if(powValue > decValue){
                notOver = false;
                maxPower -= 1;
            }
            else{
            	notOver = true;
                maxPower++;
            }
        }while(notOver);
        for(int i = maxPower; i>=0;i--){
            int maxValue = 0;
            notOver = true;
            while(notOver){
                long powValue = (long) Math.pow(theBase, i)*maxValue;
                if(powValue > decValue){
                    notOver = false;
                    decValue -= (long) Math.pow(theBase, i)*(maxValue-1);
                    result = result+convertNumToBaseLetter(maxValue-1);
                }
                else{
                    maxValue++;
                }
            }
        }
        return result;
    }
	
	/**
	 * Converts a base (up to 16) number to a base 10 number
	 * @param value The String representation of the number in up to base 16
	 * @return the base 10 value of the number
	 */
	private long convertToBaseTen(String value){
		long result = 0L;
		double pow = 0.0;
		long base = (long) theBase;
		for(int i = value.length()-1 ; i >= 0; i--){
			result += (long) (convertStringToNum(value.substring(i,i+1)) * (Math.pow(base, pow)) );
			pow++;
		}
		return result;
		
	}
	
	/**
	 * Adds the number to itself
	 * @param num the number to reverse and add
	 * @return the String representation of the sum of num and its reverse value
	 */
	private String doMath(String num){
		String num2 = flipNumber(num);
		//System.out.println(num2 +":"+num);
		long tempAddedNum = convertToBaseTen(num) + convertToBaseTen(num2);
		//System.out.println("SUM: "+tempAddedNum);
		String result = convertToBase(tempAddedNum);
		return result;
	}
	
	/**
	 * gets the Answer in the correct format
	 * @param result The found palindrome
	 * @return the String representing the answer, which may be the palindrome or NONE, [last iteration value]
	 */
	public String getAnswer(String result){
		if(noneResult.equals("no")){
			return result;
		}
		else{
			return result +", "+noneResult;
		}
	}
	
	/**
	 * Finds the palindrome or stops at the tenth iteration
	 * @return the palindrome, "NONE" if not found by the tenth iteration
	 */
	public String findPalindrome(){
		String tempNum = theNum;
		int count = 0;
		while(!isPalindrome(tempNum) && count++ < 10){
			tempNum = doMath(tempNum);
			//System.out.println(tempNum);
		}
		if(count >= 10){
			noneResult = tempNum;
			tempNum = "NONE";
		}
		else{
			noneResult = "no";
		}
		return tempNum;
	}
	
	/**
	 * Reverses the input String
	 * @param num String to be reversed
	 * @return Reversed number 
	 */
	private String flipNumber(String num){
		String newStr = "";
		for(int i = num.length()-1; i >= 0;i--){
			newStr = newStr + num.substring(i,i+1);
		}
		//System.out.println(newStr);
		return newStr;
	}
	
	/**
	 * Checks if the input String is a palindrome
	 * @param num the String to check
	 * @return true if num is a palindrome, false if not
	 */
	private boolean isPalindrome(String num){
		for(int i = 0; i < num.length()/2; i++){
			if(!num.substring(i,i+1).equals(num.substring(num.length()-1-i,num.length()-i))){
				//System.out.println(false);
				return false;
			}
		}
		return true;
	}
}
