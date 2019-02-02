package airpollutionrouting.com.mapsexample.api;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.POST;
import retrofit2.http.Query;
import airpollutionrouting.com.mapsexample.models.Directions;

/**
 * Created by Yiqun on 29/12/2018.
 *
 * Handling routing requests to Google API.
 */

public interface GetDirectionInterface {


    /**
     * Generate token for forgot password...
     *
     * @param
     */
    @POST("/maps/api/directions/json?")
    Call<Directions> getDirections(@Query("origin") String origin,
                            @Query("destination") String destination,
                            @Query("key") String apiKey,
                            @Query("waypoints") String waypoints,
                            @Query("mode") String mode,
                            @Query("alternatives") boolean alternatives);

}
