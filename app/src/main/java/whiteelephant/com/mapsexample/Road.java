package whiteelephant.com.mapsexample;

public class Road {
    private int id;
    private int crossId1;
    private int crossId2;
    private double distance;
    private double avgPollution;



    public int getCrossId1() {
        return crossId1;
    }

    public int getCrossId2() {
        return crossId2;
    }

    public double getAvgPollution() {
        return avgPollution;
    }

    public double getDistance() {
        return distance;
    }
}
