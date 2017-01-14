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

	
	// http://localhost:8080/WaitList/rest/Service/addCustomer?firstName=Jim&lastName=Flath&phoneNumber=8166741125
	@GET
	@Path("/addCustomer")
	@Produces("application/json")
	public Response addCustomer(@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@QueryParam("phoneNumber") String phoneNumber) {

		System.out.println(firstName + " - " + lastName + " - " + phoneNumber);
		if (phoneNumber.isEmpty() || firstName.isEmpty() || firstName.isEmpty())
			return Response.status(500).entity("ERROR").build();
		else {
			int waitPosition = waitListService.addCustomerToList(new Customer(firstName, lastName, phoneNumber));

			String answer = "{ \"waitPosition\": \"" + waitPosition + "\"}";
			System.out.println("Returning: " + answer);
			return Response.status(200).entity(answer).build();
		}

	}

	// http://localhost:8080/WaitList/rest/Service/customerList
	@GET
	@Path("/customerList")
	@Produces("application/json")
	public Response getList() {
		return Response.status(200).entity(waitListService.getWaitListNames())
				.build();
	}

	// http://localhost:8080/WaitList/rest/Service/customerList
	@GET
	@Path("/servicedList")
	@Produces("application/json")
	public Response getServicesList() {
		return Response.status(200).entity(waitListService.getAllListNames())
				.build();
	}

	// http://localhost:8080/WaitList/rest/Service/serviceCustomer/:UUID
	@GET
	@Path("/serviceCustomer/{uuid}")
	@Produces("application/json")
	public Response serviceCustomer(@PathParam("uuid") String uuid) {

		System.out.println("In the serviceController, again");
		waitListService.serviceCustomer(uuid);
		System.out.println("Leaving the serviceController");

		return Response.status(200).entity(waitListService.getWaitListNames())
				.build();
	}

	// http://localhost:8080/WaitList/rest/Service/serviceCustomer/:UUID
	@GET
	@Path("/deleteCustomer/{uuid}")
	@Produces("application/json")
	public Response deleteCustomer(@PathParam("uuid") String uuid) {

		System.out.println("In the deleteCustomer, again");
		waitListService.deleteCustomer(uuid);
		System.out.println("Leaving the deleteCustomer");

		return Response.status(200).entity(waitListService.getWaitListNames())
				.build();
	}

	/*
	 * {customers:[ { "customer": "1 - b b b - 11:06:25234929”}, { "customer":
	 * "2 - a a a - 11:06:30230627”}, { "customer": "3 - JIm Flath 8885551212 -
	 * 11:08:32108092”}, { "customer": "4 - arun M 4445556666 - 11:09:2851913”},
	 * ]}
	 */

	//
	// @GET
	// @Path("/callback")
	// public Response callback( @QueryParam("distance") String distance,
	// @QueryParam("pace") String pace, @QueryParam("time")String time ) {
	//
	// pace = pace.replaceAll(":", ".");
	//
	// if( pace.isEmpty() && time.isEmpty() )
	// return Response.status(500).entity( "ERROR" ).build();
	// else {
	// FitScore fitScoreAnswer = fitScoreService.calculateFitScore( distance,
	// pace, time );
	//
	// return Response.status(200).entity(
	// fitScoreAnswer.getFitScore().toString() ).build();
	// }
	//
	// }
}
