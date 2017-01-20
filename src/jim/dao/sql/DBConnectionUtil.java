package jim.dao.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jim.domain.Customer;

public class DBConnectionUtil { 

	
	public List<String> getLocationList(){
		
		String sql = "SELECT DISTINCT location FROM `waitingList` order by location asc";
		
		List<String> retVal = new ArrayList<String>();
		Connection connection = null;
		Statement statement = null;
		
		try {
			try{
				connection = getConnection();
				statement = connection.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while( result.next() ){
					retVal.add( result.getString("location") );
				}
				
			} catch( Exception e){
				e.printStackTrace();
			} finally {
				if( statement != null )
					statement.close();
				if( connection != null )
					connection.close();
			}
		} catch( Exception e) { // yeah...ugly
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	//http://www.mkyong.com/jdbc/jdbc-statement-example-insert-a-record/
	public int save(Customer customer ){
		insertCustomer( customer );
		
		return getWaitCount( customer.getLocation());
	}	
	
	public Customer getCustomerByPhone( String phone, String location){
		String sql = "Select * from waitingList where disposition='WAITING' and location='"+location+"' and phone='"+phone+"'";
		
		List<Customer> list = getCustomerList(sql);
		if( list.size() > 0 )
			return list.get(0);
		else
			return null;
//		
	}
	
	public Customer getCustomerById( String id, String location){
		String sql = "Select * from waitingList where disposition='WAITING' and location='"+location+"' and uuid='"+id+"'";
		
		List<Customer> list = getCustomerList(sql);
		if( list.size() > 0 )
			return list.get(0);
		else
			return null;	
	}
	
	public Customer getNextCustomer(String location){
		String sql = "SELECT * FROM `waitingList` WHERE waitStartTime = (select min(waitStartTime) from waitingList where disposition='waiting' and location='"+location+"')";
		System.out.println( "Get Next [" + sql +"]");
		List<Customer> list = getCustomerList(sql);
		if( list.size() > 0 )
			return list.get(0);
		else
			return null;	
	}
	
	public int getWaitCount( String location ){
		String sql = "SELECT COUNT(*) FROM waitingList where disposition='WAITING' and location='" + location +"'";
		
		int retVal=0;
		Connection connection = null;
		Statement statement = null;
		
		try {
			try{
				connection = getConnection();
				statement = connection.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if( result.next() )
					retVal = result.getInt("COUNT(*)");
				
			} catch( Exception e){
				e.printStackTrace();
			} finally {
				if( statement != null )
					statement.close();
				if( connection != null )
					connection.close();
			}
		} catch( Exception e) { // yeah...ugly
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	public Customer updateDisposition( String uuid, String location, String disposition){
		
		String sql;
		if( !disposition.equalsIgnoreCase("WAITING" ) )
			sql = "update waitingList set disposition='" + disposition + "', waitEndTime="+System.currentTimeMillis()+" where uuid='" + uuid + "' and location='" + location +"'";
		else
			sql = "update waitingList set disposition='" + disposition + "' where uuid='" + uuid + "' and location='" + location +"'";
		
		Customer retVal = null;
		
		System.out.println("updateDisposition [" + sql + "]");
		Connection connection = null;
		Statement statement = null;
		
		try {
			try{
				connection = getConnection();
				statement = connection.createStatement();
				int result = statement.executeUpdate(sql);
				retVal = getCustomerById(uuid, location);
				
			} catch( Exception e){
				e.printStackTrace();
			} finally {
				if( statement != null )
					statement.close();
				if( connection != null )
					connection.close();
			}
		} catch( Exception e) { // yeah...ugly
			e.printStackTrace();
		}
		
		return retVal;
	}
			
	public List<Customer> getActiveCustomers( String location){
		String sql = "select * from waitingList where location='"+location.toUpperCase()+"' and disposition='WAITING' order by waitStartTime";

		return getCustomerList( sql );
	}
	public List<Customer> getServicedCustomers( String location){
		String sql = "select * from waitingList where location='"+location.toUpperCase()+"' and disposition!='WAITING' order by waitStartTime";
		
		return getCustomerList( sql );
	}
	
	private List<Customer> getCustomerList( String sql ){
		
		List<Customer> list = new ArrayList<Customer>();
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			try{
				connection = getConnection();
				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				while( resultSet.next() ){
					list.add( mapResultSetToCustomer(resultSet));
				} 
				
			} catch( Exception e){
				e.printStackTrace();
				return null;
			} finally {
				if( statement != null )
					statement.close();
				if( connection != null )
					connection.close();
			}
		} catch( Exception e) { // yeah...ugly
			e.printStackTrace();
		}
		
		return list;
	}
		
	private boolean insertCustomer( Customer customer){
		String sql = "INSERT INTO `waitingList` " + mapCustomerToSQL( customer );
		try {
			return executeInsertUpdateSQL( sql );
		} catch ( Exception e){
			e.printStackTrace();
			return false;
		}
	}
	private String mapCustomerToSQL( Customer customer ){
		return "(`uuid`, `location`, `firstName`, `lastName`, `phone`, `waitStartTime`, `waitEndTime`, `disposition`, `lastNotification`) values ('" 
				+ customer.getID() + "', '"  + customer.getLocation().toUpperCase() + "', '" + customer.getFirstName() + "', '" 
				+ customer.getLastName() + "', '"  + customer.getPhoneNumber() + "', "+ customer.getWaitStartTimeInt() + ", " + customer.getWaitEndTimeInt() + ", '"  + customer.getDisposition() + "', 'UpNext')";
	}
	
	private Customer mapResultSetToCustomer( ResultSet resultSet ) throws Exception {
		String firstName = resultSet.getString("firstName");
		String lastName = resultSet.getString("lastName");
		String uuid = resultSet.getString("uuid");
		String location = resultSet.getString("location");
		String phoneNumber = resultSet.getString("phone");
		long waitStart = resultSet.getLong("waitStartTime");
		long waitEnd = resultSet.getLong("waitEndTime");
		String disposition = resultSet.getString( "disposition");
		
		Customer customer = new Customer(firstName, lastName, phoneNumber, uuid, location, waitStart, waitEnd);
		customer.setLocation(location);
		customer.setDisposition( disposition );
		
		return customer;
	}
		
	private boolean executeInsertUpdateSQL( String sql ) throws SQLException {
		
		System.out.println( "SQL = [ " + sql + "]");
		Connection connection = null;
		Statement statement = null;
		
		try{
			connection = getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			
		} catch( Exception e){
			e.printStackTrace();
			return false;
		} finally {
			if( statement != null )
				statement.close();
			if( connection != null )
				connection.close();
		}
		
		return true;
	}
	
	//https://www.mkyong.com/jdbc/how-to-connect-to-mysql-with-jdbc-driver-java/
 	private Connection getConnection(){
		System.out.println("Getting a DB connection");

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("Driver registered");
		Connection connection = null;

		try {
			System.out.println("Attempting to actually, getConnection");
			
			connection = DriverManager.getConnection("jdbc:mysql://127.9.252.130:3306/waitListUser","waitListUser", "Pass1234");
//			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/waitListUser","waitListUser", "Pass1234");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (connection != null) {
			System.out.println("It worked, returning a connection");
			return connection;
		} else {
			System.out.println("Failed to make connection!");
			return null;
		}
	}
	
}
