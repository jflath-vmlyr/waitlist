package jim.dao.sql;

import java.util.List;

import jim.domain.Customer;

public class DBTest {
	public static void main(String[] args) {
		DBConnectionUtil conn = new DBConnectionUtil();
//		Customer c = new Customer( "Jim", "Flath", "8166741125");
//		conn.save(c);
//		
//		List<Customer> customers = conn.getActiveCustomers( "abq" );
		
		conn.getLocationList();
		
		System.out.println("done");
	}

}
