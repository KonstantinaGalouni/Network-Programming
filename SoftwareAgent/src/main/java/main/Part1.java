package main;
import software_agent.*;
import threads.Threadpool;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import rest.AcceptanceRequest;
import rest.RegisterInfo;
import rest.RegisterRequest;


/** 
 * This program implements a distributed computer network scanning system,
 * which uses Nmap (Network Mapper) an opensource software 
 * 
 * @author Konstantinos Giannakelos
 * @author Konstantina Galouni
 */
public class Part1 {
	
	private static Threadpool pool;
	private static BlockingQueue<NmapJob> queue;
	private static BlockingQueue<Results> results;
	private static List<Thread> threads;
	public static ShutDown shutDown;
	public static String ip = "http://83.212.113.45:8080/AggregatorManager/services";
	//public static String ip = "http://localhost:8080/AggregatorManager/services";	
	
	/** 
	 * This is the main method of the program.
	 * <p> The Software Agent initially registers the terminal info to the 
	 * Aggregator Manager. Then periodically checks for the confirmation.
	 * When accepted, the Software Agent begins to request nmap jobs.
	 * <p>
	 * In "property.dat" there may be a number to initialize "tnum"
	 * and describes the number of one time threads in threadpool.
	 * In case there is no such number, "tnum" is initialized almost randomly.
	 * An object of Threadpool is created, as well as an object of Producer
	 *  
	 * @see Threadpool
	 * @see Producer
	 */
    public static void main(String[] args) {
    	
    	Scanner scanner = null;
    	int tnum;			// number of OneTimeJob threads in threadpool
        int waitTime = 0;	// number of seconds to wait till next request
        
        
        Random rand = new Random();
        
        try {
        	
        	scanner = new Scanner(new File(new Part1().getResource("property.dat")));	
       
	        if(scanner.hasNextInt()){	        	
	        	tnum = scanner.nextInt();
	        	
	        	if(scanner.hasNextInt()){
	        		waitTime = scanner.nextInt();
	        	}else{
	        	    waitTime = rand.nextInt(60) + 10;
	        	}
	        }else{
	            tnum = rand.nextInt(8) + 1;
	            waitTime = rand.nextInt(60) + 10;
	        }
	        
	        RegisterInfo regInfo = new RegisterInfo();  
	        regInfo.collectInfo();
	        
	        new RegisterRequest(regInfo);
	        new AcceptanceRequest(regInfo);
	        
			shutDown = new ShutDown();
			threads = shutDown.getThreads();
	        
	        pool = new Threadpool(tnum, threads);
	        queue = pool.getQueue();
	        results = pool.getResults();
	
			threads.add(Thread.currentThread());
	
	        Producer p = new Producer(waitTime, regInfo.getHash(), queue, results, threads);
	        p.startJobs();
        } catch (FileNotFoundException e) { 
			e.printStackTrace();
		}        
    }
    
	private String getResource(String filename){
    	 return getClass().getClassLoader().getResource(filename).getFile();
    }
    
}
