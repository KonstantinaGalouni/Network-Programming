package threads;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import software_agent.NmapJob;
import software_agent.Results;

/**
 * This class contains the BlockingQueue "queue" which
 * is necessary for the execution of one time jobs,
 * the BlockingQueue "results", where the results of 
 * "nmap" command are placed
 * 
 * @see OneTimeJob
 * @see PeriodicJob
 */
public class Threadpool {
	private BlockingQueue<NmapJob> queue;
	private BlockingQueue<Results> results;
	
	/**
	 * This is the constructor of a Threadpool object
     * <p>
     * The constructor initializes all the fields of Threadpool object
     * <p>
     * There are created "n" OneTimeJob threads and a Sender thread
     * 
	 * @param n the number of OneTimeJob threads
	 * @param threads a list of all threads
	 */
	public Threadpool(int n, List<Thread> threads){
		this.queue   = (BlockingQueue<NmapJob>) 
				new LinkedBlockingQueue<NmapJob>();
		this.results = (BlockingQueue<Results>) 
				new LinkedBlockingQueue<Results>();
		
		OneTimeJob ojob = new OneTimeJob(queue, results);
		for(int i=0; i < n ; i++){
			Thread t = new Thread(ojob);
            t.start();			// create and start a new OneTimeJob thread
            threads.add(t);		// add OneTimeJob thread in List "threads"
		}
		Sender sender = new Sender(results);
		Thread t = new Thread(sender);
		t.start();				// create and start a new Sender thread
		threads.add(t);			// add Sender thread in List "threads"
	}
	
	/**
	 * @return the BlockingQueue where the OneTimeJobs are placed
	 */
	public BlockingQueue<NmapJob> getQueue() {		
		return this.queue;
	}
	
	/**
	 * @return the BlockingQueue where the results of "nmap" command are placed
	 */
	public BlockingQueue<Results> getResults() {		
		return this.results;
	}
}
