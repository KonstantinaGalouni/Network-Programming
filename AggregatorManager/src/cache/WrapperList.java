package cache;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes a synchronized List of an array of Strings 
 */
public class WrapperList {
	/**
	 * The synchronized list of the class
	 */
	private List<String[]> list;
    
	/**
	 * This is the constructor of the class. The list is initialized here as an
	 * ArrayList.
	 */
    public WrapperList() { 
       this.list = new ArrayList<String[]>();
    }
    
    /**
     * Synchronized add function of the list
     * 
     * @param item the array of Strings to be added to the list
     */
    public void add(String[] item){
    	synchronized(list){
    		list.add(item);
    	}
    }
    
    /**
     * Synchronized add function of the list
     * 
     * @param pos the position of the list where the array (parameter item) is going to be added
     * @param item the array of Strings to be added to the list
     */    
    public void add(int pos, String[] item){
    	synchronized(list){
    		list.add(pos, item);
    	}
    }
    
    /**
     * Synchronized get function of the list
     * 
     * @param index index of the element to return
     * @return the element at the specified position in this list
     */
    public String[] get(int index){
    	synchronized(list){
    		return list.get(index);
    	}
    }
    
    /**
     * Synchronized size function of the list
     * 
     * @return the size of the list
     */
    public int size(){
    	synchronized(list){
    		return list.size();
    	}
    }
    
    /**
     * Synchronized function which returns the list itself
     * 
     * @return the list of the class
     */
    public List<String[]> getList(){
    	synchronized(list){
    		return list;
    	}
    }
}
