package cache;

import db.QueriesDB;

/**
 * Cache memory.
 */
public class Cache {
	
	/** Represents all accepted agents. 
	 *  <p> <b>Map Key-></b>   The hash key of Software Agent
	 *  <p> <b>Map Value-></b> An array of Strings containing the following information:
	 *	<ul>
	 *		<li> 		String[] agent = new String[9]; 				</li>	  
  	 *   	<li>		agent[0] -> Hostname							</li>
   	 *   	<li>		agent[1] -> Ip Address							</li>
   	 *   	<li>		agent[2] -> Mac Address							</li>
	 *   	<li>	   	agent[3] -> OS Version							</li>
	 *   	<li>	   	agent[4] -> Nmap Version						</li>
	 *   	<li>	   	agent[5] -> Hash Key							</li>
	 *   	<li>	   	agent[6] -> Status ( Online | Offline )			</li>
	 *  	<li>	   	agent[7] -> Time of last request				</li>
	 *  	<li>	   	agent[8] -> ID assigned by the database			</li>
	 *  </ul>
	*/
	public static Wrapper<String, String[]> acceptedMap = new Wrapper<>();
	
	
	/** Represents all pending agents. 
	 *  <p> <b>Map Key-></b>   The hash key of Software Agent
	 *  <p> <b>Map Value-></b> An array of Strings containing the following information:  
	 *	<ul>	  
	 *		<li> 		String[] agent = new String[9]; 				</li>
  	 *   	<li>		agent[0] -> Hostname							</li>
   	 *   	<li>		agent[1] -> Ip Address							</li>
   	 *   	<li>		agent[2] -> Mac Address							</li>
	 *   	<li>	   	agent[3] -> OS Version							</li>
	 *   	<li>	   	agent[4] -> Nmap Version						</li>
	 *   	<li>	   	agent[5] -> Hash Key							</li>
	 *   	<li>	   	agent[6] -> Status ( Online | Offline )			</li>
	 *  	<li>	   	agent[7] -> Time of last request				</li>
	 *  	<li>	   	agent[8] -> ID assigned by the database			</li>
	 *  </ul>
	*/
	public static Wrapper<String, String[]> pendingMap = new Wrapper<>();
	
	
	/** Represents the job results owned by each Software Agent. 
	 *  <p> <b>Map Key-></b>   The hash key of Software Agent
	 *  <p> <b>Map Value-></b> A list of String[] containing the following info:
	 *	<ul>			 		 
  	 *   	<li>	String[] results = new String[3];				</li>
   	 *   	<li>	results[0] = Job ID assigned by the database	</li>
   	 *   	<li>	results[1] = Result ID assigned by the database	</li>
	 *   	<li>	results[2] = Time when received this result</li>
	 *  </ul>
	 *  */
	public static Wrapper<String, WrapperList> resultMap = new Wrapper<>();
	
	
	/** Represents the unmarshalled results for each job. 
	 *  <p> <b>Map Key-></b>   The result ID assigned by the database.
	 *  <p> <b>Map Value-></b> A list of String[] containing the unmarshalled info
	 */
	public static Wrapper<String, WrapperList> xmlMap = new Wrapper<>();
	
	
	/** Represents the periodic jobs assigned for each Software Agent. 
	 *  <p> <b>Map Key-></b>   The hash key of Software Agent
	 *  <p> <b>Map Value-></b> A Map containing the jobs with the following description:
	 *  <ul>
	 *  	<li> <b>Map Key:</b>   The jod ID assigned by the database </li>
	 *  	<li> <b>Map Value:</b> An array of Strings containing the following information </li>  	
	 *		<ul>
	 *			<li> String[] job = new String[4]; 					</li>	  
  	 *   		<li>		job[0] -> ID assigned by the database	</li>
   	 *   		<li>		job[1] -> Execute flags					</li>
   	 *   		<li>		job[2] -> Periodic flag					</li>
	 *   		<li>	   	job[3] -> Periodic time					</li>
	 *  	</ul>
	 *  </ul>
	*/
	public static Wrapper<String, Wrapper<String,String[]>> periodicMap = new Wrapper<>();
	
	/** Represents the one time jobs assigned for each Software Agent.
	 *  <p> <b>Map Key-></b>   The hash key of Software Agent
	 *  <p> <b>Map Value-></b> A Map containing the jobs with the following description:
	 *  <ul>
	 *  	<li> <b>Map Key:</b>   The jod ID assigned by the database </li>
	 *  	<li> <b>Map Value:</b> An array of Strings containing the following information </li>
	 *		<ul>
	 *			<li> 		String[] job = new String[4]; 			</li>	  
  	 *   		<li>		job[0] -> ID assigned by the database	</li>
   	 *   		<li>		job[1] -> Execute flags					</li>
   	 *   		<li>		job[2] -> Periodic flag					</li>
	 *   		<li>	   	job[3] -> Periodic time					</li>
	 *  	</ul>
	 *  </ul>
	*/
	public static Wrapper<String, Wrapper<String,String[]>> onetimeMap = new Wrapper<>();
	
	/** Represents the newly added jobs to be assigned for each Software Agent. 
	 *  <p> <b>Map Key-></b>   The hash key of Software Agent
	 *  <p> <b>Map Value-></b> A Map containing the jobs with the following description:
	 *  <ul>
	 *  	<li> <b>Map Key:</b>   The jod ID assigned by the database </li>
	 *  	<li> <b>Map Value:</b> An array of Strings containing the following information </li>
	 *		<ul>
	 *			<li> 		String[] job = new String[4]; 			</li>	  
  	 *   		<li>		job[0] -> ID assigned by the database	</li>
   	 *   		<li>		job[1] -> Execute flags					</li>
   	 *   		<li>		job[2] -> Periodic flag					</li>
	 *   		<li>	   	job[3] -> Periodic time					</li>
	 *  	</ul>
	 *  </ul>
	*/
	public static Wrapper<String, Wrapper<String,String[]>> addMap = new Wrapper<>();
	
	/** Represents the active jobs of each Software Agent. 
	 *  <p> <b>Map Key-></b>   The hash key of Software Agent
	 *  <p> <b>Map Value-></b> A Map containing the jobs with the following description:
	 *  <ul>
	 *  	<li> <b>Map Key:</b>   The jod ID assigned by the database </li>
	 *  	<li> <b>Map Value:</b> An array of Strings containing the following information </li>
	 *		<ul>
	 *			<li> 		String[] job = new String[4]; 			</li>	  
  	 *   		<li>		job[0] -> ID assigned by the database	</li>
   	 *   		<li>		job[1] -> Execute flags					</li>
   	 *   		<li>		job[2] -> Periodic flag					</li>
	 *   		<li>	   	job[3] -> Periodic time					</li>
	 *  	</ul>
	 *  </ul>
	*/	
	public static Wrapper<String, Wrapper<String,String[]>> activeMap = new Wrapper<>();
	
	/** Represents all pending Android user accounts. 
	 *  <p> <b>Map Key-></b>   The username of the account
	 *  <p> <b>Map Value-></b> An array of Strings containing the following information:  
	 *	<ul>	  
	 *		<li> 		String[] android = new String[2]; 				</li>
  	 *   	<li>		android[0] -> username							</li>
   	 *   	<li>		android[1] -> password							</li>
	 *  </ul>
	*/
	public static Wrapper<String, String[]> pendingAndroid = new Wrapper<>();

	/** Represents all accepted Android user accounts. 
	 *  <p> <b>Map Key-></b>   The username of the account
	 *  <p> <b>Map Value-></b> An array of Strings containing the following information:  
	 *	<ul>	  
	 *		<li> 		String[] android = new String[2]; 				</li>
  	 *   	<li>		android[0] -> username							</li>
   	 *   	<li>		android[1] -> password							</li>
	 *  </ul>
	*/
	public static Wrapper<String, String[]> acceptedAndroid = new Wrapper<>();
	
	/**
	 * Initializes the cache memory fields with the database data.
	 */
	public static void init(){		
		QueriesDB qdb = new QueriesDB();		
		qdb.getSoftwareAgents();
		qdb.getUnconfirmedSA();
		qdb.getSentJobs();
		qdb.getAddJobs();
		qdb.getNmapResults();
		qdb.getXmlResults();
		qdb.getAndroid();
	}
	
}
