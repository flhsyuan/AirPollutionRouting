package airpollutionrouting.com.mapsexample.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yiqun on 15/01/2019.
 *
 * Modelling JSON object returned by Google API.
 */

public class Directions {

    public List<Routes> routes;


    public class Routes {
        public List<Legs> legs;

        @SerializedName("overview_polyline")
        public OverviewPolyline overviewPolyline;
        public String summary;
    }

    public class Legs {
        public Distance distance;
        public Duration duration;

        @SerializedName("end_address")
        public String endAddress;
        @SerializedName("start_address")
        public String startAddress;
        public List<Steps> steps;
    }

    public class Distance {
        public String text;
        public long value;
    }

    public class Steps {
        public Distance distance;
        public Duration duration;

        @SerializedName("html_instructions")
        public String htmlInstructions;

        public Polyline polyline;

        @SerializedName("start_location")
        public StartLocation startLocation;

        @SerializedName("end_location")
        public EndLocation endLocation;
    }

    public class StartLocation {
        public double lat;
        public double lng;

        public LatLng getLatLng(){
            return new LatLng(lat,lng);
        }
    }

    public class EndLocation {
        public double lat;
        public double lng;

        public LatLng getLatLng(){
            return new LatLng(lat,lng);
        }

    }

    public class Polyline {
        public String points;
    }

    public class Duration {
        public String text;
        public long value;
    }

    public class OverviewPolyline {
        public String points;
        public long value;
    }
}


