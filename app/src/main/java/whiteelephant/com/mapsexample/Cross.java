package whiteelephant.com.mapsexample;

import com.google.android.gms.maps.model.LatLng;

public class Cross {
    private String id;
    private String LatLng;


    public Cross(String id, String latLng) {
        this.id = id;
        LatLng = latLng;
    }

    public Cross() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatLng() {
        return LatLng;
    }

    public void setLatLng(String latLng) {
        LatLng = latLng;
    }

}
