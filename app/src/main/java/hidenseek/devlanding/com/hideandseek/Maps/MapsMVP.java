package hidenseek.devlanding.com.hideandseek.Maps;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import hidenseek.devlanding.com.hideandseek.Firebase.HideGame;

/**
 * Created by ted on 2/17/17.
 */

public interface MapsMVP {
    interface view{


        void askForCurrentLocationPermission();

        void addCircleAroundCurrentLocationWhereAllowedToPlay(LatLng circleAllowedToPlayIn, Integer meters);

        void updateCircleAroundCurrentLocationWhereAllowedToPlay(LatLng currentLocationOnMap, Integer meters);

        void displayErrorMsgIfOutOfHideAndSeekAreaBounds(Location location);

        void displayErrorOutOfBounds(Location currentLocation);

        void displayAlertDialogWouldYouLikeToPlayOrCreateAGame();

        void displayMapSeekingAreaSelector(String uniqueGameCode);

        void displayEnterGameCodeDialog();

        void displayNoGameExistsError(String gameCode);

        void displayGameCode(HideGame game, String s);
    }
    interface presenter{
        Location getCurrentLocation();


        void addMetersAllowedToPlay(LatLng currentLocationOnMap, Integer meters);


        boolean isMetersAllowedToPlayInAlreadyDrawn();

        void setMetersAllowedToPlayInAlreadyDrawn(boolean metersAllowedToPlayInAlreadyDrawn);

        void showErrorMsgIfOutOfHideAndSeekAreaBounds(Location location);

        boolean isUserOutSideHideAndSeekArea(Location currentLocation);

        Circle getMetersAllowedToPlayIn();

        void displayPopupWouldYouLikeToPlayOrCreateGame();

        void updateCurrentLocation(double latitude, double longitude);
    }
    interface Interactor{
       Location getCurrentLocation();


        void buildGoogleApiClient(Context context);

        boolean isUserOutSideHideAndSeekArea(Location location);
    }
    interface EasyFirebase{
        void login();

        void listenForAuthRequests();

        void unListenForAuthRequests();

        String getUUID();
    }

}
