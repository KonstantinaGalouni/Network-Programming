package rest;

import main.Part1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * The Class for registering to the Aggregator Manager.
 */
public class RegisterRequest {
	
	/** The register Web Service URI. */
	private String URI = Part1.ip + "/register"; 
	
	/**
	 * Creates and sends a new register request to the Aggregator Manager.
	 * <p> The register info is converted in JSON format.
	 * 
	 * @param regInfo {@link RegisterInfo}
	 */
	public RegisterRequest(RegisterInfo regInfo){
		
		Gson gson = new GsonBuilder().create();	         
        String regInfoJson = gson.toJson(regInfo);
        
        System.out.println("Register request ...");        
        
        Client client = Client.create();           
		WebResource webResource = client.resource(URI);			
			
		ClientResponse response = null;
		while(response == null || response.getStatus() != 200){
			try{
				response = webResource
						  .accept("application/json")
						  .type("application/json")
						  .post(ClientResponse.class,regInfoJson);
			}catch (ClientHandlerException c){
				System.out.println(response);
			}
		}		

		System.out.println("Registration successful!");
	}
}
