package services;


/**
 * This class represents the NMAP job.
 *
 */
public class NmapJob {
    private int id;
    private String params;
    private boolean periodic;
    private int time;

    /**
     * This is the constuctor of a NmapJob object
     * <p>
     * The constructor initializes all the fields of NmapJob object.
     *
     * @param id the id
     * @param params the params
     * @param periodic the periodic
     * @param time the time
     */
    public NmapJob(int id, String params, boolean periodic, int time){
        this.id = id;
        this.params = params;
        this.periodic = periodic;
        this.time = time;
    }
    
    /**
     * Prints all fields of an object NmapJob.
     */
    public void printJob(){
        System.out.println("\n***********************************************"
        		+ "**************************************************\n"
        		+ "*\tID = " + id
                + " , Flags =" + params + " , Periodic Flag = "
                + periodic + " , Periodic Time = " + time
                + "\t*\n***********************************************"
                + "**************************************************");
    }

    /**
     * Returns the id of the specified NmapJob object.
     *
     * @return      the id of the specified NmapJob object
     */
    public int getID(){
    	return id;
    }
    
    /**
     * Returns the params of the specified NmapJob object.
     *
     * @return      the params field of the specified NmapJob object
     */
    public String getParams(){
        return params;
    }
    
    /**
     * Sets the params field of the specified NmapJob object.
     *
     * @param p the new params
     */
    public void setParams(String p){
    	this.params = p;
    }

    /**
     * Returns the periodic field of the specified NmapJob object which decribes
     * if the current job is periodic (true) or not (false).
     *
     * @return      the periodic field of the specified NmapJob object
     */
    public boolean getPeriodic(){
        return periodic;
    }

    /**
     * Returns the time field of the specified NmapJob object which decribes
     * how many seconds is the periodic time of this periodic job.
     *
     * @return      the time field of the specified NmapJob object
     */
    public int getTime(){
        return time;
    }
}
