package whiteelephant.com.mapsexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by prem on 15/07/2017.
 */

public class Utils {


    public static String TAG = getLogTAG(Utils.class);

    private static String MAPS_APIKEY;

    public static String getLogTAG(Class klass) {
        return klass.getName();
    }


    /**
     * return true if string is null or empty.
     *
     * @param string
     * @return
     */
    public static boolean isStringEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }


    /**
     * return true if list is null or empty.
     *
     * @param list
     * @return
     */
    public static boolean isListEmpty(List list) {
        return list == null || list.size() == 0;
    }


    /**
     * check location is enable or not
     *
     * @param context
     * @return
     */
    public static boolean isLocationServiceEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.d(TAG, "unable to check GPS enabled " + ex.getMessage());
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.d(TAG, "unable to check networkEnabled enabled " + ex.getMessage());
        }

        return gpsEnabled && networkEnabled;
    }


    /**
     * Displays a simple alert dialog.
     */
    public static void showAlert(Context context, String head, String msg,
                                 String postiveBtnName,
                                 DialogInterface.OnClickListener positiveBtnListner,
                                 String negativeBtnName,
                                 DialogInterface.OnClickListener negativeBtnListner,
                                 boolean... cancelable) {
        AlertDialog d;
        boolean canBeClosed = (cancelable == null || cancelable.length == 0 || cancelable[0]);

        if (negativeBtnListner == null) {
            d = new AlertDialog.Builder(context).setMessage(msg)
                    .setTitle(head)
                    .setPositiveButton(postiveBtnName, positiveBtnListner)
                    .setCancelable(canBeClosed)
                    .create();
        } else {
            d = new AlertDialog.Builder(context).setMessage(msg)
                    .setTitle(head)
                    .setPositiveButton(postiveBtnName, positiveBtnListner)
                    .setNegativeButton(negativeBtnName, negativeBtnListner)
                    .setCancelable(canBeClosed)
                    .create();
        }
        d.show();
    }

    /**
     * calculate distance between 2 positions using coordinates string(LatLng)
     *
     * @param from_LatLng,to_LatLng
     * @return distance
     */

    public static Double coordinatesToDistance(String from_LatLng,String to_LatLng){
        String[] fromArray = from_LatLng.split(",");
        String[] toArray = to_LatLng.split(",");
        Double lat1 = Double.valueOf(fromArray[0]);
        Double lng1 = Double.valueOf(fromArray[1]);
        Double lat2 = Double.valueOf(toArray[0]);
        Double lng2 = Double.valueOf(toArray[1]);

        final Double Radius = 6378.137;
        Double dLat = (lat2 - lat1) * Math.PI / 180;
        Double dLng = (lng2 - lng1) * Math.PI / 180;
        Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = Radius * c;
        return distance;
    }

//    public static ArrayList<IDAndLatLng> readIDCoordinate(String path){
//        ArrayList<IDAndLatLng> idArray = new ArrayList<>();
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        FileInputStream fiStream;
//        Scanner scanner;
//
//        try{
//            fiStream = new FileInputStream(file);
//
//
//
//
//
//
//
//
//
//        }catch (NumberFormatException e){
//            e.printStackTrace();
//        }catch (FileNotFoundException e){
//            e.printStackTrace();
//        }
//
//    }

}
