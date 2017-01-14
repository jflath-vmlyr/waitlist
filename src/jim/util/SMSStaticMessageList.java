package jim.util;

import jim.domain.Customer;

public class SMSStaticMessageList {
	static int STANDARD_DELAY_MIN=5;
	static int WORKERS=2;
	
	private static String createResponse( String message ){
		return "<Message>" + message + "</Message>";
	}
	
	public static String getDeleteSMSResponse(){
		return createResponse( "You have been removed.");
	}
	
	public static String getSMSAddErrorResponse(){
		return createResponse( "We were unable to add you to the Cabela's Firearms Outfitter queue, please reply with ADD [FirstName] [LastName].");
	}
	
	public static String getStatusSMSResponse(Customer customer){
		if( customer == null ){
			return createResponse( "You are not currently on our list. Reply with ADD [FirstName] [LastName] to be added.");
		} else { 
			return createResponse( createStandardGreeting( customer) + ", you are currently number " + customer.getPosition() +" in the queue for the Cabela's Firearms Outfitter. " + getRemoveText());
		}
	}
	
	public static String getSignupMessage(Customer customer, int listSize ){
		return createStandardGreeting( customer)  + " Your appointment with a Cabela's Firearms Outfitter is in approximately " + calculateDelay( listSize ) + " minutes.  We'll text you again when it's your turn." + getRemoveText();
	}
	
	public static String getUpNextMessage( Customer customer){
		return createStandardGreeting( customer) + " You are next at the Firearms Outfitter, expect another " + calculateDelay( 1 ) + " minute delay, please head back to the Firearms Outfitter." + getRemoveText();
	}
	
	private static int calculateDelay( int listSize ){
		if( listSize == 1 )
			return STANDARD_DELAY_MIN;
		else
			return (STANDARD_DELAY_MIN * listSize) / WORKERS;
	}
	
	private static String getRemoveText(){
		return " Reply with remove to be removed from the list.";
	}
	
	private static String createStandardGreeting( Customer customer ){
		return "Hi " + customer.getFullName();
	}
}
