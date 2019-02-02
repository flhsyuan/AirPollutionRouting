package airpollutionrouting.com.mapsexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yuan and Yiqun on 15/01/2019.
 * Some useful static methods.
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
     * Calculate the distance between 2 locations given their latitudes and longitudes.
     * @param fromLat
     * @param fromLng
     * @param toLat
     * @param toLng
     * @return
     */
    public static Double coordinatesToDistance(Double fromLat, Double fromLng, Double toLat, Double toLng){
        Double lat1 = fromLat;
        Double lng1 = fromLng;
        Double lat2 = toLat;
        Double lng2 = toLng;

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

    /**
     * calculate the distance from the point to the line segment
     */

    public static double pointToLine(Double x1, Double y1, Double x2, Double y2, Double x0,
                                     Double y0) {
        double space = 0;
        double a, b, c;
        a = lineSpace(x1, y1, x2, y2);
        b = lineSpace(x1, y1, x0, y0);
        c = lineSpace(x2, y2, x0, y0);
        if (c+b == a) { // the point is on the line segment
            space = 0;
            return space;
        }
        if (c * c >= a * a + b * b) {// The target point forms an obtuse angle with the left endpoint
            space = b;
            return space;
        }

        if (b * b >= a * a + c * c) {// The target point forms an obtuse angle with the right endpoint
            space = c;
            return space;
        }

        double p = (a + b + c) / 2; // The target point forms an acute angle with the right endpoint
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));
        space = 2 * s / a;
        return space;
    }

    /**
     * calculate the distance between 2 latlngs
     */

    public static double lineSpace(double x1, double y1, double x2, double y2){
        double lineLength = 0;
        lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
                * (y1 - y2));
        return lineLength;
    }

    /**
     * translate seconds(int) to minutes(string)
     */

    public static String getMinutes(int seconds){
        double minutes = seconds/60;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(0);
        nf.setRoundingMode(RoundingMode.UP);
        String result = nf.format(minutes)+" min";
        return result ;

    }

    /**
     * translate meters(int) to KMs(string)
     */

    public static String getKMs(int meters){
        double kms = meters/1000.0;
        String resultString = String.format("%.1f",kms)+" km";
        return resultString;
    }

}
