package rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Random;

/**
 * The information need for registration of the Software Agent.
 */
public class RegisterInfo {
	
	/** The hostname. */
	private String hostname;
	
	/** The ip. */
	private String ip;
	
	/** The mac. */
	private String mac;
	
	/** The os version. */
	private String osVersion;
	
	/** The nmap version. */
	private String nmapVersion;
	
	/** The hash. */
	private int hash;

	
	/**
	 * Collect info.
	 */
	public void collectInfo(){

		InetAddress address;
        NetworkInterface network;
        Enumeration<InetAddress> addresses;
        Enumeration<NetworkInterface> networks;
       
        byte[] macInBytes;
        String ip = null;
        String mac = null;
        String hostname = null;
        String osVersion = null;
        String nmapVersion = null;
        String line;          
        	
        try {
        	
			hostname = InetAddress.getLocalHost().getHostName();			
            networks = NetworkInterface.getNetworkInterfaces();
            
            while(networks.hasMoreElements()) {
            	
                network = networks.nextElement();
                addresses = network.getInetAddresses();
                
                while(addresses.hasMoreElements()) {
                    address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && 
                    		address instanceof Inet4Address) {
                    	ip = address.getHostAddress();
                    	break;
                    }
                }
                
                if(ip!=null){
                	macInBytes = network.getHardwareAddress();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < macInBytes.length; i++) {
                        sb.append(String.format("%02X%s", macInBytes[i], 
                        		(i < macInBytes.length - 1) ? "-" : ""));
                    }
                    mac = sb.toString();
                    break;
                }
            }
            
            osVersion = System.getProperty("os.name") +
            			" " +
            			System.getProperty("os.version");            
            

            Process p = Runtime.getRuntime().exec("nmap -V");
            BufferedReader input =  new BufferedReader(
            						new InputStreamReader(p.getInputStream()));            
         
            while ((line = input.readLine()) != null) {
            	
            	String s[] = line.split(" ");
            	
            	for(int i=0; i<s.length; i++){
            		if(s[i].equals("version")){
            			nmapVersion = s[i+1];
            			break;
            		}
            	}
            	
            	if(nmapVersion!=null){
            		break;
            	}            	
            }
            input.close();
    	} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
        Random r = new Random();
        
		this.hostname = hostname;
		this.ip = ip;
		this.mac = mac;
		this.osVersion = osVersion;
		this.nmapVersion = nmapVersion;
		this.hash = (hostname+" "+ip+" "+mac+" "+osVersion+" "+nmapVersion).hashCode();
		//this.hash += r.nextInt(10000) + 1 ;
	}
	
	/**
	 * Gets the hostname.
	 *
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Sets the hostname.
	 *
	 * @param hostname the new hostname
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Sets the ip.
	 *
	 * @param ip the new ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Gets the mac.
	 *
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * Sets the mac.
	 *
	 * @param mac the new mac
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * Gets the os version.
	 *
	 * @return the os version
	 */
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * Sets the os version.
	 *
	 * @param osVersion the new os version
	 */
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	/**
	 * Gets the nmap version.
	 *
	 * @return the nmap version
	 */
	public String getNmapVersion() {
		return nmapVersion;
	}

	/**
	 * Sets the nmap version.
	 *
	 * @param nmapVersion the new nmap version
	 */
	public void setNmapVersion(String nmapVersion) {
		this.nmapVersion = nmapVersion;
	}

	
	/**
	 * Gets the hash.
	 *
	 * @return the hash
	 */
	public int getHash() {
		return hash;
	}

	/**
	 * Sets the hash.
	 *
	 * @param hash the new hash
	 */
	public void setHash(int hash) {
		this.hash = hash;
	}
}
