package services;


/**
 * The Class XmlResults.
 */
public class XmlResults {
		private int nmapJobId;
		private String xml;
		
		/**
		 * Instantiates a new xml results.
		 *
		 * @param nmapJobId the nmap job id
		 * @param xml the xml
		 */
		public XmlResults(int nmapJobId, String xml){
			this.nmapJobId = nmapJobId;
			this.xml = xml;
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
		 * Gets the xml.
		 *
		 * @return the xml
		 */
		public String getXml() {
			return xml;
		}
		
		/**
		 * Sets the xml.
		 *
		 * @param xml the new xml
		 */
		public void setXml(String xml) {
			this.xml = xml;
		}
}
