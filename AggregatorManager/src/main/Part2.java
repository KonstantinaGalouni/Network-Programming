package main;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import cache.Cache;
import db.Database;
import gui.GUI;


/**
 * This program implements the Web Services for the Aggregator Manager and 
 * the Graphical User Interface for the administrator.
 * 
 * @author Konstantinos Giannakelos
 * @author Konstantina Galouni
 * @author Marilena Michou
 */
public class Part2 {
	
	//public static String BASE_URI = "http://83.212.113.45:8080/AggregatorManager/";	
	public static String BASE_URI = "http://localhost:8080/AggregatorManager/";	
	public static HttpServer server;
	
	/**
	 * Starts the HTTP server.
	 *
	 * @return the <b>HTTP</b> server
	 */
	public static HttpServer startServer() {
		final ResourceConfig rc = new ResourceConfig().packages("services");
		
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI),
				rc);
	}
	

	/**
	 * Starts the Web Application.
	 * <ul>
	 * 		<li> 	Creates the {@link Database} 		</li>
	 * 		<li> 	Initializes the {@link Cache} 		</li>
	 * 		<li> 	Starts the Glassfish Web Server 	</li>
	 * 		<li> 	Starts the Admin GUI 				</li>
	 * </ul>
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		
		new Database();				
		Cache.init();	
		new GUI();	
		
		server = startServer();
		System.out.println("Initiated grizzly server!");		
	}
	
}