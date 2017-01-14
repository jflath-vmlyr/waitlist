package jim.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jim.domain.Customer;
import jim.util.SMSClient;
import jim.util.SMSStaticMessageList;

public class WaitListService {

	private static Map<String, Customer> customerMap = Collections.synchronizedMap( new LinkedHashMap<String,Customer>());
	private static Map<String, Customer> servicedMap = Collections.synchronizedMap( new LinkedHashMap<String,Customer>());
	
	public int addCustomerToList( Customer customer ){
		customerMap.put(customer.getID(), customer);
		
		if( customerMap.size() > 1 ){
			sendSMS(customer, SMSStaticMessageList.getSignupMessage(customer, customerMap.size() ) );
		} else {
			notifyNext();
		}
		return customerMap.size(); 
	}
	
	public Customer getCustomerByPhone( String phone ){
		Iterator<Customer> it = customerMap.values().iterator();
		int position = 1;
		while( it.hasNext()){
			Customer c = it.next();
			if( phone.equals( c.getPhoneNumber() ) ){
				c.setPosition(position);
				return c;
			}
			position++;
		}
		return null;
	}
	
	public List<Customer> getWaitListNames(){
		List<Customer> list = new ArrayList<Customer>(customerMap.values());
		
		return list;
	}
	
	public List<Customer> serviceCustomer( String uuid ){
		System.out.println("In the serviceController service");
		Customer c = customerMap.remove(uuid);
		c.setDisposition("SERVICED");
		servicedMap.put(uuid, c);
		
		notifyNext();
		
		return getWaitListNames();		
	}
	
	private void notifyNext(){
		if( !getWaitListNames().isEmpty() ) {
			System.out.println("About to call sendSMS");
			Customer next = getWaitListNames().get(0);
			if( !next.isNotifiedNext() ){
				sendSMS( next, SMSStaticMessageList.getUpNextMessage( next ) );
				next.setNotifiedNext();
			}
			System.out.println("Back from call sendSMS");
		}
	}
	
	public void deleteCustomerByPhone( String phone ){
		concreteDeleteCustomerByPhone( phone );
		notifyNext();
	}
	
	public void concreteDeleteCustomerByPhone(String phone){
		System.out.println("In the delete by phone");
		Iterator<Customer> it = customerMap.values().iterator();
		String uuid = null;
		while( it.hasNext()){
			Customer c = it.next();
			System.out.println("Checking customer " + c.getPhoneNumber());
			if( phone.equals( c.getPhoneNumber() ) ){
				System.out.println("Found a match");
				uuid=c.getID();
				break;
			}
		}
		
		System.out.println("Out of the loop");
		if( uuid != null ) {
			System.out.println("UUID was not null, going to delete");
			Customer c = customerMap.remove(uuid);
			c.setDisposition("DELETED via Text");
			servicedMap.put(uuid, c);
			System.out.println("Done with the delete");
			concreteDeleteCustomerByPhone( phone);
		}
	}
	
	public List<Customer> deleteCustomer( String uuid ){
		System.out.println("In the deleteCustomer service");
		Customer c = customerMap.remove(uuid);
		c.setDisposition("DELETED");
		servicedMap.put(uuid, c);
		
		notifyNext();
		return getWaitListNames();		
	}
	
	public List<Customer> getAllListNames(){
		List<Customer> list = new ArrayList<Customer>(servicedMap.values());
		
		return list;
	}
	
	private void sendSMS( Customer customer, String message ){
		
		System.out.println("In sendSMS");
		SMSClient smsClient = new SMSClient();
		System.out.println("Client created");
		
		System.out.println("Sending SMS to " + customer.getPhoneNumber());
		smsClient.sendMessage(customer.getPhoneNumber(), message);
		System.out.println( "Message sent");
	}
	
}
