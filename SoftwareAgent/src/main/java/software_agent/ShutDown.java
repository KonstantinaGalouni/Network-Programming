package software_agent;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a List "threads" of all threads
 * 
 * @see threads.Threadpool
 * @see threads.PeriodicJob
 */
public class ShutDown {
	private List<Thread> threads;
	
	/**
	 * This is the constructor of a ShutDown object
     * <p>
     * There is a ShutdownHook in order to catch 
     * a "Crtl-C" signal and terminate the application normally. 
     * This is why "cleanup" method is called
     */
	public ShutDown(){
		this.threads = new ArrayList<Thread>();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                cleanup();
            }
        }));
	}
	
	/**
	 * After a termination signal, this method is called to 
	 * terminate the application normally.
	 * <p>
	 * All threads are interrupted and joined before the
	 * program terminates and all queues and list are set
	 * to null, in order to be destroyed by garbage collector
	 * as soon as possible
	 */
	public void cleanup(){
		System.out.println("\nStarting cleanup ...\n");

		try {
			if(threads != null){
				for(Thread t : threads){
					if(Thread.currentThread().getId() != t.getId()){
						t.interrupt();		//interrupt all threads
					}
				}
				for(Thread t : threads){
					if(Thread.currentThread().getId() != t.getId()){
						t.join();			//join (wait to terminate) all threads
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threads = null;
		
		System.out.println("\nFinished cleanup!");
	}
	
	/**
	 * @return the List where all threads are stored in order to terminate normally
	 */
	public List<Thread> getThreads(){
		return this.threads;
	}
}
