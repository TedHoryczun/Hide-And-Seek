package hidenseek.devlanding.com.hideandseek;

import android.content.Context;
import android.location.Location;

/**
 * Created by ted on 2/17/17.
 */

public interface MapsMVP {
    interface view{

        void createMarketAtCurrentLocation(Location currentLocation);
    }
    interface presenter{
        Location getCurrentLocation();

        void getLocationUpdatesEveryMillisecond(Context context, int millisecond);
    }
    interface Interactor{
       Location getCurrentLocation();

        void buildGoogleApiClient(Context context);
    }

}
