package jim.controller;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import jim.domain.Customer;
import jim.service.WaitListService;

@Path("/Service")
public class WaitListController {

	@Context
	private HttpServletRequest servletRequest;

	static DecimalFormat df = new DecimalFormat("00.00");

	private WaitListService waitListService = new WaitListService();

	// http://localhost:8080/WaitList/services/Service/addCustomer?firstName=Jim&lastName=Flath&phoneNumber=8166741125
	@GET
	@Path("/addCustomer")
	@Produces("application/json")
	public Response addCustomer(@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@QueryParam("phoneNumber") String phoneNumber,
			@QueryParam("location") String location) {

		System.out.println(firstName + " - " + lastName + " - " + phoneNumber);
		if (phoneNumber.isEmpty() || firstName.isEmpty() || firstName.isEmpty())
			return Response.status(500).entity("ERROR").build();
		else {
			int waitPosition = waitListService.addCustomerToList(new Customer(firstName, lastName, phoneNumber, location));

			String answer = "{ \"waitPosition\": \"" + waitPosition + "\"}";
			System.out.println("Returning: " + answer);
			return Response.status(200).entity(answer).build();
		}

	}

	// http://localhost:8080/WaitList/services/Service/customerList
	@GET
	@Path("/customerList/{location}")
	@Produces("application/json")
	public Response getList(@PathParam("location") String location) {
		return Response.status(200).entity(waitListService.getWaitListNames(location))
				.build();
	}

	@GET
	@Path("/locationList")
	@Produces("application/json")
	public Response getLocationList(){
		return Response.status(200).entity(waitListService.getLocationList()).build();
	}
	
	// http://localhost:8080/WaitList/services/Service/customerList
	@GET
	@Path("/servicedList/{location}")
	@Produces("application/json")
	public Response getServicedList(@PathParam("location") String location ) {
		return Response.status(200).entity(waitListService.getFinishedListNames( location )).build();
	}

	// http://localhost:8080/WaitList/services/Service/serviceCustomer/:UUID
	@GET
	@Path("/serviceCustomer/{location}/{uuid}")
	@Produces("application/json")
	public Response serviceCustomer(@PathParam("location") String location, @PathParam("uuid") String uuid) {

		System.out.println("In the serviceController, again");
		waitListService.serviceCustomer(uuid, location);
		System.out.println("Leaving the serviceController");

		return Response.status(200).entity(waitListService.getWaitListNames(location)).build();
	}
	
	// http://localhost:8080/WaitList/services/Service/serviceNext/:location
	@GET
	@Path("/serviceNext/{location}")
	@Produces("application/json")
	public Response serviceNextCustomer(@PathParam("location") String location) {

		System.out.println("In the serviceController, again");
		Customer customer = waitListService.serviceNextCustomer(location.toUpperCase());
		System.out.println("Leaving the serviceController");

		if( customer != null )
			return Response.status(200).entity(customer).build();
		else
			return Response.status(404).entity("no customers").build();
	}	

	// http://localhost:8080/WaitList/rest/Service/serviceCustomer/:UUID
	@GET
	@Path("/deleteCustomer/{location}/{uuid}")
	@Produces("application/json")
	public Response deleteCustomer(@PathParam("uuid") String uuid, @PathParam("location") String location) {

		System.out.println("In the deleteCustomer, again");
		waitListService.deleteCustomer(uuid, location);
		System.out.println("Leaving the deleteCustomer");

		return Response.status(200).entity(waitListService.getWaitListNames(location))
				.build();
	}
}
