package src;

public class MatchSticks {
	
	// the amount of sticks it takes to display all numbers is 48.
	// thereby, it's impossible to display all numbers twice.
	// however, it is possible to display the number One 40 times, as
	// the maximum sticks is 80. So in terms of digits, the most digits
	// we can get is 40 digits. 
	// we must check if we need to long this. 
	
	// in terms of creating the large numbers, we will likely use
	// recursion. the only other option is to find some way that
	// enables us to find out how many numbers using some sort of modular
	// formula. We should take the number of sticks we have and teset u
	private int[] numberSticks = { 6,2,5,5,4,5,6,3,7,5 };
	private int numberOfSticks;
	
	public MatchSticks(int num){
		numberOfSticks = num;
	}
	
	
}
