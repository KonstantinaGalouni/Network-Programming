package threads;

import java.io.BufferedReader;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import main.Part1;
import software_agent.Results;
import software_agent.XmlResults;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * This class implements a sender thread, which takes results 
 * of "nmap" commands in XML format from a BlockingQueue periodically
 * and prints them in stdout 
 * 
 * @see OneTimeJob
 * @see PeriodicJob
 */
public class Sender implements Runnable {
	private BlockingQueue<Results> results;
	
    /**
     * This is the constructor of a Sender object
     * <p>
     * The constructor initializes the BlockingQueue
     * 
     * @param  results the BlockingQueue where the results 
	 * 		   of "nmap" command are placed
     */
	public Sender(BlockingQueue<Results> results){
		this.results = results;
	}
	
	/** 
	 * Takes all the available results of "nmap" commands in XML format 
	 * from BlockingQueue results periodically every sleepSec seconds,
	 * prints them in stdout, and sends them to the Aggregator Manager.
	 */
    public void run(){
    	int sleepSec;	//sleeping seconds
    	String line;
    	BufferedReader input;
    	try{
    		Random rand = new Random();
    		while(!Thread.currentThread().isInterrupted()){
    			sleepSec = rand.nextInt(30) + 10;
    			Results res = results.take();
				input = res.getBufferedReader();

				// print XML results from "nmap" command
				System.out.println("\n");
				String xml = "";
				while ((line = input.readLine()) != null) {
					xml = xml + line + "\n";
		        }
				input.close();
                
                Client client = Client.create();
    	        String URI =  Part1.ip + "/sendresults";            
    			WebResource webResource = client.resource(URI);			
    			
    			XmlResults xmlRes = new XmlResults(res.getNmapJobId(), xml);
    			Gson gson = new GsonBuilder().create();
    			String result = gson.toJson(xmlRes);
    			
    			ClientResponse response = webResource
						  .accept("application/json")
						  .type("application/json")
						  .post(ClientResponse.class,result.toString());  
    			
    			if(response.getStatus() != 200) {
    				throw new RuntimeException("Failed: HTTP error code : "
    												+ response.getStatus());
    			}
		        
				//sleep before checking for new available results
    			Thread.sleep(1000*sleepSec);
    		}
    	}catch (Exception e){
			System.out.println("Sender   thread exited normally" );
    	}
    }
}
