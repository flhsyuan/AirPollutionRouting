package airpollutionrouting.com.mapsexample.events;

import airpollutionrouting.com.mapsexample.models.Directions;

/**
 * Created by prem on 16/07/2017.
 */

public class DirectionsEvent {

    public Directions _directions;
    public boolean isError = false;

    public DirectionsEvent(Directions directions){
        _directions = directions;
    }
}
