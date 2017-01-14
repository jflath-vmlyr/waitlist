package jim.test;


public class Test {

	public static void main(String[] args) {
		for( double d=8.00; d<8.60; d+=0.01){
			System.out.println( d + " == total == " + calcIt( d ));	
		}
	}
	
	private static double calcIt( double d ){
		
		double decimal = ((int)( d * 100.00))%100;
		double percentage = (float)(decimal)/60f;
		int wholeNumber =  (int)Math.abs(d - (int) (decimal/100.00)); 
		
		System.out.println("Whole Number == " + wholeNumber + " && Percentage == " + percentage) ;
		
		return wholeNumber+percentage;
	}
	
}
