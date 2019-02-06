package airpollutionrouting.com.mapsexample;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import airpollutionrouting.com.mapsexample.models.Cross;
import airpollutionrouting.com.mapsexample.models.Road;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import airpollutionrouting.com.mapsexample.api.GetDirectionInterface;
import airpollutionrouting.com.mapsexample.events.DirectionsEvent;
import airpollutionrouting.com.mapsexample.models.Directions;

/**
 * Created by Yiqun and Yuan on 12/01/2019.
 *
 * Handling the user input(start location & destination) and manipulate requests sent to Google
 * Map API.
 */

public class GetDirectionApiService extends IntentService {


    private static final String PARAM_FROM_LAT = "from_lat";
    private static final String PARAM_FROM_LNG = "from_lng";
    private static final String PARAM_TO_LAT = "to_lat";
    private static final String PARAM_TO_LNG = "to_lng";
    private static final String PARAM_APIKEY = "apiKey";
    private static final String AIR_OR_NORMAL = "AIR";  //yuan
    static final String TAG = Utils.getLogTAG(GetDirectionApiService.class);

    @Override
    public void onCreate() {
        super.onCreate();

        BusProvider.getInstance().register(this);
    }

    public GetDirectionApiService() {
        super("GetDirectionApiService");
    }

    public static void getPossibleDirections(@NonNull Context context, @NonNull LatLng fromLatLng,
                                             @NonNull LatLng toLatLng, @NonNull String mapsAPIKey,
                                             @NonNull String airOrNormal) {
        Intent intent = new Intent(context, GetDirectionApiService.class);
        intent.putExtra(PARAM_FROM_LAT, fromLatLng.latitude);
        intent.putExtra(PARAM_FROM_LNG, fromLatLng.longitude);
        intent.putExtra(PARAM_TO_LAT, toLatLng.latitude);
        intent.putExtra(PARAM_TO_LNG, toLatLng.longitude);
        intent.putExtra(PARAM_APIKEY, mapsAPIKey);
        intent.putExtra(AIR_OR_NORMAL,airOrNormal);
        context.startService(intent);
    }


    /**
     * send requests to Google Map API using retrofit.
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            // read all info from intent
            Log.d(TAG, "Handling intent");
            Double toLat = intent.getDoubleExtra(PARAM_TO_LAT, 0.0);
            Double toLng = intent.getDoubleExtra(PARAM_TO_LNG, 0.0);
            Double fromLat = intent.getDoubleExtra(PARAM_FROM_LAT, 0.0);
            Double fromLng = intent.getDoubleExtra(PARAM_FROM_LNG, 0.0);
            String apiKey = intent.getStringExtra(PARAM_APIKEY);
            String mode = intent.getStringExtra(AIR_OR_NORMAL);

            // manipulate latitude and longitude of start location and destination.
            final String origin = fromLat + ", " + fromLng;
            String destination = toLat + ", " + toLng;
            Log.d(TAG, "origin =  " + origin + ", destination : " + destination + ", APIKey : " + apiKey);

            Road nearestRoadofStart = getNearestRoad(fromLat, fromLng);   //yuan
            Road nearestRoadofEnd = getNearestRoad(toLat, toLng);   //yuan

            ArrayList<Road> allRoads = MapsActivity._allRoads;

            // Send requests according to the type of service.
            // "NORMAL" means returning nearest path without considering the air pollution index.
            if (mode.equals("NORMAL")) {
                ArrayList<String> pathList = new AStarSearch(allRoads).findpath(fromLat, fromLng, toLat, toLng, nearestRoadofStart, nearestRoadofEnd,false);

                String waypoints = "";
                for (int i = 1; i < pathList.size() - 1; i++) {
                    String waypoint = idToLatLng(pathList.get(i));
                    waypoints += waypoint + "|";
                }
                waypoints = waypoints.substring(0, waypoints.length() - 1);

                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                GetDirectionInterface apiService = retrofit.create(GetDirectionInterface.class);
                Call<Directions> call = apiService.getDirections(origin, destination, apiKey, waypoints, "walking", false);
                call.enqueue(new Callback<Directions>() {
                    @Override
                    public void onResponse(Call<Directions> call, Response<Directions> response) {
                        Log.d(TAG, "Hey... Got a response");

                        DirectionsEvent directions = new DirectionsEvent(response.body());
                        BusProvider.getInstance().post(directions);
                    }

                    @Override
                    public void onFailure(Call<Directions> call, Throwable t) {

                    }
                });
            }
            // returning optimized air pollution path considering the PM 2.5 index.
            else {

                ArrayList<String> pathList = new AStarSearch(allRoads).findpath(fromLat, fromLng, toLat, toLng, nearestRoadofStart, nearestRoadofEnd,true);

                String waypoints = "";
                for (int i = 1; i < pathList.size() - 1; i++) {
                    String waypoint = idToLatLng(pathList.get(i));
                    waypoints += waypoint + "|";
                }
                waypoints = waypoints.substring(0, waypoints.length() - 1);


                // add all selected nodes to the waypoints and then send the request
                if (fromLat != 0.0 && fromLng != 0.0 && toLat != 0.0 && toLng != 0.0 && apiKey != null) {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com")
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    GetDirectionInterface apiService = retrofit.create(GetDirectionInterface.class);
                    Call<Directions> call = apiService.getDirections(origin, destination, apiKey, waypoints, "walking", false);
                    Log.d(TAG, "onHandleIntent: the waypoints are " + waypoints);
                    call.enqueue(new Callback<Directions>() {
                        @Override
                        public void onResponse(Call<Directions> call, Response<Directions> response) {
                            Log.d(TAG, "Hey... Got a response");

                            DirectionsEvent directions = new DirectionsEvent(response.body());
                            BusProvider.getInstance().post(directions);
                        }

                        @Override
                        public void onFailure(Call<Directions> call, Throwable t) {

                        }
                    });
                } else {
                    Log.d(TAG, " some values are unexpected");
                }


            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        BusProvider.getInstance().unregister(this);
    }

    /**
     * Read the cross csv which contains latlngs of crosses.
     * @return ArrayList<Cross>
     */
    public ArrayList<Cross> readCrossCSV() {
        ArrayList<Cross> crossArray = new ArrayList<>();
        InputStream is = null;
        AssetManager assetManager = getBaseContext().getAssets();
        try {
            is = assetManager.open("cross.csv");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "readCrossCSV: failed to read cross");
        }

        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        StringTokenizer st = null;
        try {

            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                Cross obj = new Cross();
                obj.setId(st.nextToken());
                obj.setLatLng(st.nextToken().replace("\"",""));
                crossArray.add(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crossArray;
    }

    /**
     * Read the pollution csv.
     * @return ArrayList<Road>
     */
    public ArrayList<Road> readPollutionCSV() {
        ArrayList<Road> roadArray = new ArrayList<>();
        InputStream is = null;
        AssetManager assetManager = getBaseContext().getAssets();
        try {
            is = assetManager.open("pollution.csv");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "readCSV: failed to read pollution");
        }

        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        StringTokenizer st = null;
        try {

            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                Road obj = new Road();
                obj.setFromCrossID(st.nextToken());
                obj.setToCrossID(st.nextToken());
                double fromLat = Double.valueOf(st.nextToken());
                double fromLng = Double.valueOf(st.nextToken());
                double toLat = Double.valueOf(st.nextToken());
                double toLng = Double.valueOf(st.nextToken());
                obj.setFromLat(fromLat);
                obj.setFromLng(fromLng);
                obj.setToLat(toLat);
                obj.setToLng(toLng);
                obj.setPollutionIndex(Double.valueOf(st.nextToken()));
                obj.setDistance(Utils.coordinatesToDistance(fromLat,fromLng,toLat,toLng));
                roadArray.add(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return roadArray;
    }



    /**
     * return the nearest road to the given position
     */

    public Road getNearestRoad(Double originLat, Double originLng){
        Road target = new Road();
        ArrayList<Road> roads = MapsActivity._allRoads;

        Map<String,Double> map = new HashMap<>();

        for(Road road:roads){
            String mapKey = road.getFromCrossID()+","+road.getToCrossID();
            double distance = Utils.pointToLine(road.getFromLat(),road.getFromLng(),
                    road.getToLat(),road.getToLng(),
                    originLat,originLng);
            map.put(mapKey,distance);
        }

        // sort the list
        List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());   //ascending
            }
        });
        Map.Entry<String, Double> obj = list.get(0);
        String twoNodeNumber = obj.getKey();
        String[] fromAndTo= twoNodeNumber.split(",");
        String fromNum = fromAndTo[0];
        String toNum = fromAndTo[1];

        for (Road road:roads){
            if(road.getFromCrossID().equals(fromNum) && road.getToCrossID().equals(toNum)){
                target.setFromCrossID(road.getFromCrossID());
                target.setToCrossID(road.getToCrossID());
                target.setFromLat(road.getFromLat());
                target.setToLat(road.getToLat());
                target.setFromLng(road.getFromLng());
                target.setToLng(road.getToLng());
                target.setPollutionIndex(road.getPollutionIndex());
                target.setDistance(road.getDistance());
            }
        }
        return target;

//        Map<String,Double> map = new HashMap<>();
//
//        Log.d(TAG, "onHandleIntent: successfully read the csvs ");
//
//        // get the map key:latlng value:distance
//        for(Cross cross:crosses){
//        String latlng = cross.getLatLng();
//        //get the revised origin
//        String revisedOrigin = origin.replace(",","_");
//            Double distance = Utils.coordinatesToDistance(latlng,revisedOrigin);
//            map.put(latlng,distance);
//            Log.d(TAG, "getNearestCross: the distance is "+distance);
//        }
//
//        // sort the map and get the 2 nearest latlng
//        List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());
//        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
//            @Override
//            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
//                return o1.getValue().compareTo(o2.getValue());   //ascending
//            }
//        });
//        Log.d(TAG, "getNearestCross: the list is "+list.toString());
//
//
//        for(int i=0;i<2;i++){
//            Map.Entry<String, Double> obj = list.get(i);
//            String latlng = obj.getKey();
//            String revisedLatlng = latlng.replace("_",",");
//            nearestCrosses.add(obj.getKey());
//        }
    }

    /**
     * return the latitude and longitude of the given Cross ID
     * @param id
     * @return
     */
    public String idToLatLng(String id){
        ArrayList<Cross> crossArray = readCrossCSV();
        String latLng ="";
        for (Cross cross:crossArray){
            if (id.equals(cross.getId())){
                latLng = cross.getLatLng();
            }
        }
        String revisedlatLng = latLng.replace("_",",");
        return revisedlatLng;
    }

}
