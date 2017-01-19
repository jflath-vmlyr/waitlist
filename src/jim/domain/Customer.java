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
	private String location;
	private UUID uuid = java.util.UUID.randomUUID();
	
	private String disposition="WAITING";
	private static SimpleDateFormat sTimeFormat = new SimpleDateFormat("hh:mm:ss");
	private static SimpleDateFormat sDateTimeformat = new SimpleDateFormat("M/DD/yy hh:mm:ss");

	
	private boolean notifiedNext=false;
	
	private boolean isPersistent=false;
	
	public long getWaitStartTimeInt(){
		return waitStartTime.getTime();
	}
	
	public long getWaitEndTimeInt(){
		
		if( waitEndTime == null )
			return 999;
		else
			return waitEndTime.getTime();
	}
	
	
	public String getFullName(){
		return firstName + " " + lastName;
	}
	
	public int getPosition(){
		return this.position;
	}
	
	public void setPosition(int position){
		this.position = position;
	}
	

	public Customer( String firstName, String lastName, String phoneNumber, String location){
		this.firstName=firstName;
		this.lastName=lastName;
		setPhoneNumber(phoneNumber);
		this.location=location;
		this.waitStartTime=GregorianCalendar.getInstance().getTime();
	}

	public Customer( String firstName, String lastName, String phoneNumber, String id, String location, long waitStart, long waitEnd){
		this.firstName=firstName;
		this.lastName=lastName;
		setPhoneNumber(phoneNumber);
		this.uuid=java.util.UUID.fromString(id);
		this.location=location;
		
		this.waitStartTime = new Date( waitStart);

		if( waitEnd != 999){
			this.waitEndTime= new Date(waitEnd);
			System.out.println("WaitEndTime = " + this.waitEndTime);
		}
		
	}
	
	@JsonIgnore
	public String getWaitStartTimeAndDate(){
		return sDateTimeformat.format(waitStartTime);
	}
	
	@JsonIgnore
	public String getWaitEndTimeAndDate(){
		return sDateTimeformat.format(waitEndTime);
	}
	
	public String getID(){
		return uuid.toString();
	}
	
	public String getWaitStartTime(){
		return sTimeFormat.format(waitStartTime);
	}
	
	@JsonIgnore
	public String getWaitEndTime(){
		if( waitEndTime != null)
			return sTimeFormat.format(waitEndTime);
		else 
			return getWaitStartTime();
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
	}
	
	public void setNotifiedNext(){
		notifiedNext=true;
	}
	
	public boolean isNotifiedNext(){
		return notifiedNext;
	}

	public boolean isPersistent() {
		return isPersistent;
	}

	public void setPersistent(boolean isPersistent) {
		this.isPersistent = isPersistent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location.toUpperCase();
	}
}
