package cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class describes a synchronized Map, which key type "Key" and value type "Value" 
 *
 * @param <Key> the type of the key in the Map map
 * @param <Value> the type of the value in the Map map
 */
public class Wrapper <Key, Value> {
	/**
	 * The synchronized map of the class
	 */
    private Map<Key, Value> map;
    
    /**
     * This is the constructor of the class. The map is initialized here as a
     * LinkedHashMap.
     */
    public Wrapper() { 
       this.map = new LinkedHashMap<Key, Value>();
    }
    
    /**
     * Synchronized put function of the map
     * 
     * @param key the key of the map where the value is going to be put
     * @param value	the value to be put
     */
    public void put(Key key, Value value){
    	synchronized(map){
    		map.put(key, value);
    	}
    }
    
    /**
     * Synchronized get function of the map
     * 
     * @param key the key of the map which is going to be searched for
     * @return the value which corresponds to the key parameter
     */
    public Value get(Key key){
    	Value value = null;
    	synchronized(map){
    		value = map.get(key);
    	}
    	return value;
    }
    
    /**
     * Synchronized containsKey function of the map
     * 
     * @param key the key of the map which is going to be searched for
     * @return true if the key parameter belongs to the map, otherwise false
     */
    public Boolean containsKey(Key key){
    	synchronized(map){
    		if(map.containsKey(key)){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Synchronized entrySet function of the map
     * 
     * @return the entrySet of the map
     */
    public Set<Entry<Key, Value>> entrySet(){
    	Set<Entry<Key, Value>> entry;
    	synchronized(map){
    		entry = map.entrySet();
    	}
    	return entry;
    }
    
    /**
     * Synchronized remove function of the map
     * 
     * @param key the key of the map which is going to be searched and deleted
     */
    public void remove(Key key){
    	synchronized(map){
    		map.remove(key);
    	}
    }
    
    /**
     * Synchronized function which returns the map itself
     * 
     * @return the synchronized map
     */
    public Map<Key, Value> getMap(){
    	synchronized(map){
    		return map;
    	}
    }

    /**
     * Set the map of the class
     * 
     * @param newMap the new map which is going to be assigned to map
     */
    public void setMap(LinkedHashMap<Key, Value> newMap){
		map = newMap;
    }
}
