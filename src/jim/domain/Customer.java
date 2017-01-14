package jim.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Customer {
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private Date waitStartTime;
	private Date waitEndTime;
	private int position;
	
	private boolean notifiedNext=false;
	
	private UUID uuid = java.util.UUID.randomUUID();
	
	private String disposition="WAITING";
	private static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
	
	public String getFullName(){
		return firstName + " " + lastName;
	}
	
	public int getPosition(){
		return this.position;
	}
	
	public void setPosition(int position){
		this.position = position;
	}
	

	public Customer( String firstName, String lastName, String phoneNumber){
		this.firstName=firstName;
		this.lastName=lastName;
		setPhoneNumber(phoneNumber);
		this.waitStartTime=GregorianCalendar.getInstance().getTime();
	}
	
	public String getID(){
		return uuid.toString();
	}
	
	public String getWaitStartTime(){
		return sdf.format(waitStartTime);
	}
	
	@JsonIgnore
	public String getWaitEndTime(){
		return sdf.format(waitEndTime);
	}
	
	public String getWaitDuration(){
		long startTime = waitStartTime.getTime();
		long endTime;
		
		if( waitEndTime != null)
			endTime = waitEndTime.getTime();
		else
			endTime = GregorianCalendar.getInstance().getTimeInMillis();
		
		long duration = (endTime - startTime);
		
		return String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(duration),
			    TimeUnit.MILLISECONDS.toSeconds(duration) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
			);
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		String tmp = phoneNumber.replaceAll("[^0-9.]", "");
		
		if( tmp.startsWith("1"))
			this.phoneNumber = tmp;
		else
			this.phoneNumber = "1"+tmp;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
		this.waitEndTime = GregorianCalendar.getInstance().getTime();
	}
	
	public void setNotifiedNext(){
		notifiedNext=true;
	}
	
	public boolean isNotifiedNext(){
		return notifiedNext;
	}
}
