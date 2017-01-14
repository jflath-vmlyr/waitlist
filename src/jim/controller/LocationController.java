package jim.controller;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public class LocationController {
	

	@GET
	@Path("/waitList/{location}")
	public Response viewWaitList(@PathParam("location")String location ){
		try{
			return Response.temporaryRedirect( new URI("../index.html?location="+location)).build();
		} catch( Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
