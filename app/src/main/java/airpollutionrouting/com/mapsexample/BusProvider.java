package airpollutionrouting.com.mapsexample;

import com.squareup.otto.Bus;

/**
 * Created by Yuan on 11/01/2019.
 *
 */

public final class BusProvider {
    private static final Bus busInstance = new Bus();

    public static Bus getInstance() {
        return busInstance;
    }
}