package whiteelephant.com.mapsexample;

import com.google.android.gms.maps.model.LatLng;

public class Cross {
    private int id;
    private String LatLng;

    public Cross(int id) {
        this.id = id;
    }

    public Cross(int id, String latLng) {
        this.id = id;
        LatLng = latLng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatLng() {
        return LatLng;
    }

    public void setLatLng(String latLng) {
        LatLng = latLng;
    }

}
