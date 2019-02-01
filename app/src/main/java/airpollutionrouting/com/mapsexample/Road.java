package airpollutionrouting.com.mapsexample;

import okhttp3.internal.Util;

import static airpollutionrouting.com.mapsexample.Utils.coordinatesToDistance;

public class Road {

    private String fromCrossID;
    private String ToCrossID;
    private Double fromLat;
    private Double fromLng;
    private Double toLat;
    private Double toLng;
    private Double pollutionIndex;
    private Double distance;


    public Road(String fromCrossID, String toCrossID, Double fromLat, Double fromLng, Double toLat, Double toLng, Double pollutionIndex) {
        this.fromCrossID = fromCrossID;
        ToCrossID = toCrossID;
        this.fromLat = fromLat;
        this.fromLng = fromLng;
        this.toLat = toLat;
        this.toLng = toLng;
        this.pollutionIndex = pollutionIndex;

        this.distance=coordinatesToDistance(fromLat,fromLng,toLat,toLng);
    }

    public Road() {

    }

    public String getFromCrossID() {
        return fromCrossID;
    }

    public void setFromCrossID(String fromCrossID) {
        this.fromCrossID = fromCrossID;
    }

    public String getToCrossID() {
        return ToCrossID;
    }

    public void setToCrossID(String toCrossID) {
        ToCrossID = toCrossID;
    }

    public Double getFromLat() {
        return fromLat;
    }

    public void setFromLat(Double fromLat) {
        this.fromLat = fromLat;
    }

    public Double getFromLng() {
        return fromLng;
    }

    public void setFromLng(Double fromLng) {
        this.fromLng = fromLng;
    }

    public Double getToLat() {
        return toLat;
    }

    public void setToLat(Double toLat) {
        this.toLat = toLat;
    }

    public Double getToLng() {
        return toLng;
    }

    public void setToLng(Double toLng) {
        this.toLng = toLng;
    }

    public Double getPollutionIndex() {
        return pollutionIndex;
    }

    public void setPollutionIndex(Double pollutionIndex) {
        this.pollutionIndex = pollutionIndex;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Road{" +
                "fromCrossID='" + fromCrossID + '\'' +
                ", ToCrossID='" + ToCrossID + '\'' +
                ", fromLat=" + fromLat +
                ", fromLng=" + fromLng +
                ", toLat=" + toLat +
                ", toLng=" + toLng +
                ", pollutionIndex=" + pollutionIndex +
                ", distance=" + distance +
                '}';
    }
}
