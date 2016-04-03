package rest;

import main.Part1;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


/**
 * The Class for requesting the confirmation from Aggregator Manager.
 */
public class AcceptanceRequest {	
	
	/** The acceptance Web Service URI */
	private String URI = Part1.ip + "/acceptance"; 
	
	/**
	 * Requests from server the confirmed status.
	 * <p> If the response is:
	 * <ul>
	 * <li>an accept HTTP status (202) the SA proceeds to request nmap jobs<li>
	 * <li>a pending HTTP status (102) the SA continues to request an acceptance status</li>
	 * <li>a decline HTTP status (101) the SA registers again <li>
	 * </ul>
	 * 
	 * @param regInfo {@link RegisterInfo}
	 */
	public AcceptanceRequest(RegisterInfo regInfo){
		
		Client client = Client.create();
		ClientResponse response = null;      
		WebResource webResource = client.resource(URI + "/" + regInfo.getHash());		
		
		while(response == null || response.getStatus() != 202){
			
			System.out.println("Check if accepted ...");

			if(response != null && response.getStatus() == 101){
				new RegisterRequest(regInfo);
			}
			
			try{
				Thread.sleep(3000);
				response = webResource.get(ClientResponse.class);
			}catch (ClientHandlerException c){
				System.out.println(response);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Got accepted!");
	}
}
