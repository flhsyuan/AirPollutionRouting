package whiteelephant.com.mapsexample;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import whiteelephant.com.mapsexample.api.GetDirectionInterface;
import whiteelephant.com.mapsexample.events.DirectionsEvent;
import whiteelephant.com.mapsexample.models.Directions;

/**
 * Created by prem on 15/07/2017.
 */

public class GetDirectionApiService extends IntentService {


    private static final String PARAM_FROM_LAT = "from_lat";
    private static final String PARAM_FROM_LNG = "from_lng";
    private static final String PARAM_TO_LAT = "to_lat";
    private static final String PARAM_TO_LNG = "to_lng";
    private static final String PARAM_APIKEY = "apiKey";
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
                                             @NonNull LatLng toLatLng, @NonNull String mapsAPIKey) {
        Intent intent = new Intent(context, GetDirectionApiService.class);
        intent.putExtra(PARAM_FROM_LAT, fromLatLng.latitude);
        intent.putExtra(PARAM_FROM_LNG, fromLatLng.longitude);
        intent.putExtra(PARAM_TO_LAT, toLatLng.latitude);
        intent.putExtra(PARAM_TO_LNG, toLatLng.longitude);
        intent.putExtra(PARAM_APIKEY, mapsAPIKey);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Log.d(TAG, "Handling intent");
            Double toLat = intent.getDoubleExtra(PARAM_TO_LAT, 0.0);
            Double toLng = intent.getDoubleExtra(PARAM_TO_LNG, 0.0);
            Double fromLat = intent.getDoubleExtra(PARAM_FROM_LAT, 0.0);
            Double fromLng = intent.getDoubleExtra(PARAM_FROM_LNG, 0.0);
            String apiKey = intent.getStringExtra(PARAM_APIKEY);

            final String origin = fromLat + ", " + fromLng;
            String destination = toLat + ", " + toLng;

            Log.d(TAG, "origin =  " + origin + ", destination : " + destination + ", APIKey : " + apiKey);

//            List<String> nearest2Crosses = getNearestCross(origin);

            //read road csv
            System.out.println("test");

            ArrayList<Road> allRoads = readPollutionCSV();
            ArrayList<String> pathList = new AStarSearch(allRoads).findpath("5","44");
            for(String node: pathList){
                System.out.println(node);
            }

//            Log.d(TAG, "onHandleIntent: the 2 points are "+nearest2Crosses);

            //TODO
            // 1.对于起点和终点，各自找到距离最近的cross点坐标
            // 2.找到的cross用AStar,找到必经的cross
            // 3.把必经的cross加入到waypoints里面
            if (fromLat != 0.0 && fromLng != 0.0 && toLat != 0.0 && toLng != 0.0 && apiKey != null) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                GetDirectionInterface apiService = retrofit.create(GetDirectionInterface.class);
                //TODO the latlng need to be changed
                Call<Directions> call = apiService.getDirections(origin, destination, apiKey, "-37.8136,144.9654", "walking", false);
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
     * return 2 nearest cross to the given position
     */

    public ArrayList<String> getNearestCross(Double originLat, Double originLng){
        ArrayList<String> nearestCrosses = new ArrayList<>();
        ArrayList<Road> roads = readPollutionCSV();

        for(Road road: roads){
            int retval1 = road.getFromLat().compareTo(originLat); //>0
            int retval2 = road.getFromLng().compareTo(originLng); //<0
            int retval3 = road.getFromLat().compareTo(originLat); //<0
            int retval4 = road.getFromLat().compareTo(originLat); //>0
            if ( retval1>0 && retval2<0 && retval3<0 && retval4>0){
                Log.d(TAG, "getNearestCross: the lat and lng is "+ road.getFromLat()+" "+road.getFromLng()+" the to latlng is "+road.getToLat()+""+road.getToLng());
                nearestCrosses.add(road.getFromCrossID());
                nearestCrosses.add(road.getToCrossID());
            }
        }

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
        return nearestCrosses;
    }

}
