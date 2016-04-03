package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import software_agent.NmapJob;
import software_agent.Results;

/**
 * This class implements an OneTimeJob thread, which
 * takes one time jobs from the BlockingQueue "queue", 
 * executes via Runtime the "nmap" command and
 * puts the results in another BlockingQueue "results".
 * <p>
 * As long as "results" is no more empty, Sender class
 * is responsible to print results in stdout.
 * <p> 
 * Each OneTimeJob thread belongs to the threadpool 
 * 
 * @see Threadpool
 * @see Sender
 */
public class OneTimeJob implements Runnable{
	
	private BlockingQueue<?> queue;
	private BlockingQueue<Results> results;
	private BufferedReader input;
    
	/**
	 * This is the constructor of an OneTimeJob object
     * <p>
     * The constructor initializes the BlockingQueue "queue"
     * and the BlockingQueue "results"
     * 
	 * @param queue the BlockingQueue where the OneTimeJobs are placed
	 * @param results the BlockingQueue where 
	 * the results of "nmap" command are placed
	 */
    public OneTimeJob(BlockingQueue<?> queue, BlockingQueue<Results> results){
    	this.queue = queue;
        //System.out.println("OneTimeJob thread with ID = 
		//			" + this.getId() + " has been started!");
        this.results = results;
    }
    
    /**
     * Takes all the available one time jobs from the BlockingQueue "queue"
     * and executes via Runtime the "nmap" command.
     * Afterwards, it puts the results in another BlockingQueue "results",
     * so that they can be printed by Sender class
     */
    public void run(){
    	Process p = null;
    	try{
    		while(!Thread.currentThread().isInterrupted()){
		    	NmapJob njob = (NmapJob) queue.take();
		    	System.out.println("\nOneTime thread with ID = " 
		    		+ Thread.currentThread().getId() + " took job with ID = "
		    					+ njob.getID() + "\n");
		    	
		        String command = "sudo nmap " + njob.getParams();	    	
				//execute "nmap" command as superuser
		        p = Runtime.getRuntime().exec(command);
				//wait the command to end before putting result in queue "results"
		        if(p.waitFor() == 0){
		        	input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        	Results res = new Results(njob.getID(), input);
		        	results.put(res);
		        }
		    }
		    
    	}catch (InterruptedException ie){
    		if(p!=null){
    			p.destroy();
    		}
    		System.out.println("OneTime  thread with ID = " 
    				+ Thread.currentThread().getId() + " exited normally" );
    		return;
	    } catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
