package software_agent;

import java.io.BufferedReader;

/**
 * The Class Results.
 */
public class Results {
	
	private int nmapJobId;
	private BufferedReader br;
	
	/**
	 * Instantiates a new results.
	 *
	 * @param nmapJobId the nmap job id
	 * @param br the br
	 */
	public Results(int nmapJobId, BufferedReader br){
		this.nmapJobId = nmapJobId;
		this.br = br;
	}
	
	/**
	 * Gets the nmap job id.
	 *
	 * @return the nmap job id
	 */
	public int getNmapJobId() {
		return nmapJobId;
	}
	
	/**
	 * Sets the nmap job id.
	 *
	 * @param nmapJobId the new nmap job id
	 */
	public void setNmapJobId(int nmapJobId) {
		this.nmapJobId = nmapJobId;
	}
	
	/**
	 * Gets the buffered reader.
	 *
	 * @return the buffered reader
	 */
	public BufferedReader getBufferedReader() {
		return br;
	}
	
	/**
	 * Sets the buffered reader.
	 *
	 * @param br the new buffered reader
	 */
	public void setBufferedReader(BufferedReader br) {
		this.br = br;
	}
}
