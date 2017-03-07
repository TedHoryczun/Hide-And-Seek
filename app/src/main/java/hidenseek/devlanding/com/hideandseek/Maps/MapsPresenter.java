package hidenseek.devlanding.com.hideandseek.Maps;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ted on 2/17/17.
 */

public class MapsPresenter implements MapsMVP.presenter{
    private final MapsMVP.view view;
    private final MapsMVP.Interactor interactor;
    private LocationRequest mLocationRequest;
    public boolean metersAllowedToPlayInAlreadyDrawn;
    public boolean outOfBoundsErrorCurrentlyBeingDisplayed = false;
    public Circle metersAllowedToPlayIn;

    public MapsPresenter(MapsMVP.view view, Context context){
        this.view = view;
        this.interactor = new MapsCurrentLocation(this, context);
    }

    public MapsPresenter(MapsMVP.view view, MapsMVP.Interactor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public Location getCurrentLocation() {
        return interactor.getCurrentLocation();
    }

    public void askToReceiveCurrentLocationPermission() {
        view.askForCurrentLocationPermission();
    }

    @Override
    public void addMetersAllowedToPlay(LatLng currentLocationOnMap, Integer meters) {
        if(isMetersAllowedToPlayInAlreadyDrawn() == true){
            view.updateCircleAroundCurrentLocationWhereAllowedToPlay(currentLocationOnMap, meters);
        }else{
            view.addCircleAroundCurrentLocationWhereAllowedToPlay(currentLocationOnMap, meters);
        }

    }

    @Override
    public boolean isMetersAllowedToPlayInAlreadyDrawn() {
        return metersAllowedToPlayInAlreadyDrawn;
    }

    @Override
    public void setMetersAllowedToPlayInAlreadyDrawn(boolean metersAllowedToPlayInAlreadyDrawn) {
        this.metersAllowedToPlayInAlreadyDrawn = metersAllowedToPlayInAlreadyDrawn;
    }

    @Override
    public void showErrorMsgIfOutOfHideAndSeekAreaBounds(Location location) {
        if(isUserOutSideHideAndSeekArea(location)){
            view.displayErrorMsgIfOutOfHideAndSeekAreaBounds(location);
        }
    }

    public void showErrorOutOfHideAndSeekAreaBounds() {
        if(outOfBoundsErrorCurrentlyBeingDisplayed == false){
            Location currentLocation = getCurrentLocation();
            view.displayErrorOutOfBounds(currentLocation);
        }
    }

    @Override
    public boolean isUserOutSideHideAndSeekArea(Location currentLocation) {
        boolean isUserOutSideArea = false;
        if(interactor.isUserOutSideHideAndSeekArea(currentLocation)){
            isUserOutSideArea = true;

        }
        return isUserOutSideArea;
    }

    @Override
    public Circle getMetersAllowedToPlayIn() {
        return metersAllowedToPlayIn;
    }

    public void setMetersAllowedToPlayIn(Circle metersAllowedToPlayIn) {
        this.metersAllowedToPlayIn = metersAllowedToPlayIn;
    }

    @Override
    public void displayPopupWouldYouLikeToPlayOrCreateGame() {
        view.displayAlertDialogWouldYouLikeToPlayOrCreateAGame();

    }

    public void playGame() {
    }

    public void createGame() {
        view.displayMapSeekingAreaSelector();
    }
}
