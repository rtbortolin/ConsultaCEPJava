package main.java.consultaCEP.web.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.time.StopWatch;

import main.java.consultaCEP.domain.entities.Address;
import main.java.consultaCEP.domain.services.CorreiosWebAccess;
import main.java.consultaCEP.domain.services.SearchCepService;
import main.java.consultaCEP.infra.db.AddressRepository;
import main.java.consultaCEP.interfaces.ISearchCepService;

@Path("/cep")
public class CepController {

	private static ISearchCepService service;

	public CepController() {
		if (service == null) {
			service = new SearchCepService(new CorreiosWebAccess(),
					new AddressRepository());
			System.out.println("ISearchCepService instantiated");
		}
	}

	@Path("{cep}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCep(@PathParam("cep") String cep) {
		StopWatch sw = new StopWatch();
		sw.start();
		Address address = service.getAddress(cep);

		Response response = Response.status(200).entity(address).build();
		Response corsResponse = createCorsResponse(response);
		
		sw.stop();
		System.out.println("Request time: " + cep + " | " + sw);
		return corsResponse;
	}

	private Response createCorsResponse(Response contResp) {
		ResponseBuilder resp = Response.fromResponse(contResp);
		resp.header("Access-Control-Allow-Origin", "*").header(
				"Access-Control-Allow-Methods", "GET, POST, OPTIONS");

		return resp.build();
	}
}