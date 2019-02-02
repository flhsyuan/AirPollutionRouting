package airpollutionrouting.com.mapsexample.events;

import android.location.Address;

/**
 * Created by Yuan on 15/01/2019.
 */

public class AddressFetchedEvent {
    public Address _address;
    public boolean isError = false;

    public AddressFetchedEvent(Address address){
        _address =address;
    }
}
