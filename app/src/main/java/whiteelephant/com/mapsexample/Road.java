package whiteelephant.com.mapsexample;

import okhttp3.internal.Util;

public class Road {

    private String fromCrossID;
    private String ToCrossID;
    private String fromLatlng;
    private String toLatlng;
    private Double pollutionIndex;
    private Double distance;


    public Road(String fromCrossID, String toCrossID,
                String fromLatlng, String toLatlng,
                Double pollutionIndex) {
        this.fromCrossID = fromCrossID;
        ToCrossID = toCrossID;
        this.fromLatlng = fromLatlng;
        this.toLatlng = toLatlng;
        this.pollutionIndex = pollutionIndex;
        this.distance = Utils.coordinatesToDistance(fromLatlng,toLatlng);
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

    public String getFromLatlng() {
        return fromLatlng;
    }

    public void setFromLatlng(String fromLatlng) {
        this.fromLatlng = fromLatlng;
    }

    public String getToLatlng() {
        return toLatlng;
    }

    public void setToLatlng(String toLatlng) {
        this.toLatlng = toLatlng;
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
                ", fromLatlng='" + fromLatlng + '\'' +
                ", toLatlng='" + toLatlng + '\'' +
                ", pollutionIndex=" + pollutionIndex +
                ", distance=" + distance +
                '}';
    }


}
