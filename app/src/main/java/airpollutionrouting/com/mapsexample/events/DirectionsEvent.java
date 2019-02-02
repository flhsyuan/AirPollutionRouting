package airpollutionrouting.com.mapsexample.events;

import airpollutionrouting.com.mapsexample.models.Directions;

/**
 * Created by Yuan on 15/01/2019.
 */

public class DirectionsEvent {

    public Directions _directions;
    public boolean isError = false;

    public DirectionsEvent(Directions directions){
        _directions = directions;
    }
}
