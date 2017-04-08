package hidenseek.devlanding.com.hideandseek.Maps;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import hidenseek.devlanding.com.hideandseek.Firebase.EasyFirebase;
import hidenseek.devlanding.com.hideandseek.Firebase.Firebaselistener;
import hidenseek.devlanding.com.hideandseek.GameCode;

/**
 * Created by ted on 2/17/17.
 */

public class MapsPresenter implements MapsMVP.presenter {
    private final MapsMVP.view view;
    private final MapsMVP.Interactor interactor;
    private Context context;
    private LocationRequest mLocationRequest;
    public boolean metersAllowedToPlayInAlreadyDrawn;
    public boolean outOfBoundsErrorCurrentlyBeingDisplayed = false;
    public Circle metersAllowedToPlayIn;
    private EasyFirebase firebase;

    public MapsPresenter(MapsMVP.view view, Context context) {
        this.context = context;
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
        if (isMetersAllowedToPlayInAlreadyDrawn() == true) {
            view.updateCircleAroundCurrentLocationWhereAllowedToPlay(currentLocationOnMap, meters);
        } else {
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
        if (isUserOutSideHideAndSeekArea(location)) {
            view.displayErrorMsgIfOutOfHideAndSeekAreaBounds(location);
        }
    }

    public void showErrorOutOfHideAndSeekAreaBounds() {
        if (outOfBoundsErrorCurrentlyBeingDisplayed == false) {
            Location currentLocation = getCurrentLocation();
            view.displayErrorOutOfBounds(currentLocation);
        }
    }

    @Override
    public boolean isUserOutSideHideAndSeekArea(Location currentLocation) {
        boolean isUserOutSideArea = false;
        if (interactor.isUserOutSideHideAndSeekArea(currentLocation)) {
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

    @Override
    public void updateCurrentLocation(double latitude, double longitude) {
        firebase.updateCurrentLocation(latitude, longitude);
    }

    public void playGame() {
        view.displayEnterGameCodeDialog();

    }

    public void createGame() {
        final String uniqueGameCode = GameCode.getUniqueGameCode();
        firebase.isGameCodeAlreadyUsed(uniqueGameCode, new Firebaselistener() {
            @Override
            public void onSuccess() {
                createGame();
            }

            @Override
            public void onError() {
                firebase.createGame(uniqueGameCode);
            }
        });
        view.displayMapSeekingAreaSelector();
    }

    public void loginToFirebase() {
        firebase.login();

    }

    public void startFirebase() {
        firebase = new EasyFirebase(context);
    }

    public void stopFirebase() {
        firebase.unListenForAuthRequests();
    }

    public void enterGame(final String gameCode) {
        firebase.joinGame(gameCode, new Firebaselistener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
               view.displayNoGameExistsError(gameCode);
            }
        });


    }

    public void leaveAllGames() {
        firebase.leaveAllGames();
    }
}
