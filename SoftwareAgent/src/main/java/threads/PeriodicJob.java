package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

import software_agent.NmapJob;
import software_agent.Results;

/**
 * This class implements a PeriodicJob thread, which
 * executes via Runtime the "nmap" command periodically
 * and puts the results in a BlockingQueue "results".
 * <p>
 * As long as "results" is no more empty, Sender class
 * is responsible to print results in stdout.
 * 
 * @see Threadpool
 * @see Sender
 */
public class PeriodicJob implements Runnable {
    private NmapJob njob;
    private String command;
    private BlockingQueue<Results> results;
    private BufferedReader input;

    /**
     * This is the constructor of a PeriodicJob object
     * <p>
     * The constructor initializes the BlockingQueue "results"
     * and the NmapJob which contains the information about
     * the "nmap" command
     * 
     * @param njob the NmapJob which contains the information about
     * the "nmap" command
     * @param results the BlockingQueue where the results 
	 * of "nmap" command are placed
     */
    public PeriodicJob(NmapJob njob, BlockingQueue<Results> results){
        this.njob = njob;
        this.command = "sudo nmap " + this.njob.getParams();
        this.results = results;
    }

    /**
     * Executes via Runtime the "nmap" command periodically every 
     * njob.getTime() seconds and puts the results in the BlockingQueue 
     * "results",so that they can be printed by Sender class
     */
    public void run(){
    	Process p = null;
    	try {
    		System.out.println("\nPeriodic thread with ID = " 
		    		+ Thread.currentThread().getId() + " took job with ID = "
		    					+ njob.getID() + "\n");
    		while (!Thread.currentThread().isInterrupted()){
	            //execute "nmap" command as superuser
                p = Runtime.getRuntime().exec(command);	
				//wait the command to end before putting result in queue "results"
                if(p.waitFor() == 0){	
                	input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                	Results res = new Results(njob.getID(), input);
                	results.put(res);
                }

				//sleep before rerun the "nmap" command
                Thread.sleep(1000*njob.getTime());	
            }
    	}catch (InterruptedException ie) {
    		if(p!=null){
    			p.destroy();
    		}
            System.out.println("Periodic thread with ID = " 
            		+ Thread.currentThread().getId() + " exited normally" );
        	return;
        }catch (IOException e) {
    		e.printStackTrace();
        }
    }
}
