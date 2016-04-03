package utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
/**
 * The Class NetworkUtils is the notification about the connection to the Internet.
 */
public class NetworkUtils {
     
    /** The type wifi. */
    public static int TYPE_WIFI = 1;
    
    /** The type mobile. */
    public static int TYPE_MOBILE = 2;
    
    /** The type not connected. */
    public static int TYPE_NOT_CONNECTED = 0;     
     
    /**
     * Gets the connectivity status.
     *
     * @param context the context
     * @return the connectivity status
     */
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
 
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                return TYPE_WIFI;
            }
             
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                return TYPE_MOBILE;
            }
        } 
        return TYPE_NOT_CONNECTED;
    }
     
    /**
     * Gets the connectivity status string.
     *
     * @param context the context
     * @return the connectivity status string
     */
    public static String getConnectivityStatusString(Context context) {
        int con = getConnectivityStatus(context);
        String status = null;
        if (con == TYPE_WIFI) {
            status = "Wifi enabled";
        }else if (con == TYPE_MOBILE) {
            status = "Mobile 3G enabled";
        }else if (con == TYPE_NOT_CONNECTED) {
            status = "No connection to Internet";
        }
        return status;
    }
}