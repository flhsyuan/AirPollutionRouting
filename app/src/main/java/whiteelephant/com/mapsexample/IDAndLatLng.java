package whiteelephant.com.mapsexample;

import com.google.android.gms.maps.model.LatLng;

public class IDAndLatLng {
    private int id;
    private String LatLng;


    public IDAndLatLng(int id, String latLng) {
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
