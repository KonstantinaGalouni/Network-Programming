package software_agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import main.Part1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import threads.OneTimeJob;
import threads.PeriodicJob;
import threads.Threadpool;

/**
 * This class requests jobs from the Aggregator Manager for execution.
 * 
 * @see NmapJob
 * @see Threadpool
 * @see OneTimeJob
 * @see PeriodicJob
 */
public class Producer{
	
	private int waitTime;
	private int hashKey;
    private BlockingQueue<NmapJob> queue = null;
    private BlockingQueue<Results> results = null;
    private List<Thread> threads;
    private Map<Integer, Thread> periodicJobs;
    

    /**
     * This is the constructor of a Producer object
     * <p>
     * The constructor initializes all the fields of Producer object
     * 
     * @param  waitTime the time to wait before requesting the next batch of jobs
     * @param  hashKey the identification key of the Software Agent
     * @param  queue the BlockingQueue where the OneTimeJobs are placed
     * @param  results the BlockingQueue where the results 
	 * 		   of "nmap" command are placed
     * @param  threads the List where all threads 
	 * 		   are stored to terminate normally 
     */
    public Producer(int waitTime, int hashKey, BlockingQueue<NmapJob> queue, 
				BlockingQueue<Results> results, List<Thread> threads){
    	
        this.waitTime = waitTime;
        this.hashKey  = hashKey;
        this.queue    = queue;
        this.results  = results;
        this.threads  = threads;
        this.periodicJobs = new HashMap<Integer, Thread>();
    }

    /** 
	 * Requests jobs from the Aggregator Manager.
	 * If the job is :
	 * <ul>
	 * <li> periodic, the SA creates a new {@link PeriodicJob} thread 	  </li>
	 * <li> one time, the SA inserts the job in the {@link Producer#queue}, 
	 * 				then	a {@link OneTimeJob} thread consumes the job. </li>
	 * <li> deleting, the SA interrupts the thread executing the job 	  </li>
	 * <li> exiting, the SA creates a new thread invoking System.exit(0); </li>
	 * </ul>
     */
    public void startJobs(){
        
        System.out.println("Wait time = " + waitTime);       
        
        try {          
            while (!Thread.currentThread().isInterrupted()) {
            	
            	System.out.println("Requesting jobs");
            	
                Client client = Client.create();
    	        String URI = Part1.ip + "/requestjobs";            
    			WebResource webResource = client.resource(URI);	
    			ClientResponse response = null;
    			
    			try{	
	    			response = webResource
							  .accept("application/json")
							  .type("application/json")
							  .post(ClientResponse.class,Integer.toString(hashKey));  
    			}catch (ClientHandlerException c){
    				System.out.println(response);
    			}
    			
    			if(response == null || response.getStatus() != 200) {    				
    				Thread.sleep(1000*waitTime);
    				continue;
    			}
    			

    			String output = response.getEntity(String.class);
                
    			Gson gson = new GsonBuilder().create();
    			NmapJob[] jobs = gson.fromJson(output,NmapJob[].class);
    			
    			for(NmapJob njob : jobs){
    				
    				njob.printJob();
    				
    				if(njob.getParams().contains("Stop")){
    					System.out.println("GOT A DELETING JOB");
    					
    					Thread t = periodicJobs.get(njob.getID());
    					if(threads.contains(t)){
	    					t.interrupt();
	    					threads.remove(t);
	    					periodicJobs.remove(njob.getID());  
    					}
    				}else if(njob.getParams().contains("exit(0)")){
    					System.out.println("GOT AN EXITING JOB");
    					
    					Thread t = new Thread(new Runnable() {  
    						public void run()   { System.exit(0);}
    					});
    					t.start();
    				}else{
	                    if(njob.getPeriodic() == true){
	                        final PeriodicJob pjob = new PeriodicJob(njob, results);
	
	                        Thread t = new Thread(pjob);
							// create and start a new periodic thread
	                        t.start();
	                        
							// add Periodic thread in List "threads"
	                        threads.add(t);
	                        
	                        // add nmapJob id and thread in map "periodicJobs"	                        
	                        periodicJobs.put(njob.getID(), t);
	                    }else{
							// put one time job in threadpool's queue
	                    	queue.put(njob);
	                    }
    				}
                }
                Thread.sleep(1000*waitTime);	//sleep before reading more jobs
            }
        }catch (InterruptedException ie){
			queue   = null;
			results = null;
        }finally {
            System.out.println("Producer thread exited normally");
        }

    }
}
