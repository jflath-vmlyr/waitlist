package jim.service;

import java.util.List;

import jim.dao.sql.DBConnectionUtil;
import jim.domain.Customer;
import jim.util.SMSClient;
import jim.util.SMSStaticMessageList;

public class WaitListService {

	private List<Customer> waitListNames = null;
	private List<Customer> finishedMap = null;
	
	
	public List<String> getLocationList(){
		DBConnectionUtil dbConn = new DBConnectionUtil();
		List<String> retVal = dbConn.getLocationList();
		
		return retVal;
	}
	
	public int addCustomerToList( Customer customer ){

		DBConnectionUtil dbConn = new DBConnectionUtil();
		int waitCount = dbConn.save(customer);
		
		if( waitCount > 1 ){
			sendSMS(customer, SMSStaticMessageList.getSignupMessage(customer, waitCount ) );
		} else {
			notifyNext( customer.getLocation());
		}
		return waitCount; 
	}
	
	public Customer getCustomerByPhone( String phone, String location ){
		Customer c = new DBConnectionUtil().getCustomerByPhone( phone, location);

		return c;
	}
	
	public List<Customer> getWaitListNames(String location ){
		if( waitListNames == null )
			waitListNames = new DBConnectionUtil().getActiveCustomers(location);
		
		return waitListNames;
	}
	
	public Customer serviceCustomer( String uuid, String location ){
		System.out.println("In the serviceController service");
		// update the customer by UUID and set disposition==SERVICED
		DBConnectionUtil dbConn = new DBConnectionUtil();
		Customer c = dbConn.updateDisposition( uuid, location, "SERVICED");
		
		notifyNext(location);
		
		return c;		
	}
	
	public Customer serviceNextCustomer( String location ){
		System.out.println("In the serviceController service");
		// update the customer by UUID and set disposition==SERVICED
		DBConnectionUtil dbConn = new DBConnectionUtil();
		Customer customer = dbConn.getNextCustomer( location );
		if( customer != null )
			serviceCustomer(customer.getID(), location);
		
		return customer;		
	}
	
	private void notifyNext(String location){
		if( !getWaitListNames(location).isEmpty() ) {
			System.out.println("About to call sendSMS");
			Customer next = getWaitListNames(location).get(0);
			if( !next.isNotifiedNext() ){
				sendSMS( next, SMSStaticMessageList.getUpNextMessage( next ) );
				next.setNotifiedNext();
			}
			System.out.println("Back from call sendSMS");
		}
	}
	
	public void deleteCustomerByPhone( String phone, String location ){
		concreteDeleteCustomerByPhone( phone, location );
		notifyNext(location);
	}
	
	public void concreteDeleteCustomerByPhone(String phone, String location){
		DBConnectionUtil dbUtil = new DBConnectionUtil();
		Customer customer = dbUtil.getCustomerByPhone(phone, location);
		if( customer != null )
			dbUtil.updateDisposition(customer.getID(), location, "DELETED FROM TEXT");
	}
	
	public List<Customer> deleteCustomer( String uuid, String location ){
		System.out.println("In the deleteCustomer service");
		DBConnectionUtil dbUtil = new DBConnectionUtil();
		dbUtil.updateDisposition(uuid, location, "DELETED");
		
		notifyNext(location);
		return getWaitListNames(location);	

	}
	
	public List<Customer> getFinishedListNames(String location){
		return new DBConnectionUtil().getServicedCustomers(location);
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
