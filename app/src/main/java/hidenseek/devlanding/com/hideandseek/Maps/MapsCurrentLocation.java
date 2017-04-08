package hidenseek.devlanding.com.hideandseek.Maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Circle;

/**
 * Created by ted on 2/17/17.
 */

public class MapsCurrentLocation implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, MapsMVP.Interactor {

    private final MapsMVP.presenter presenter;
    private final Context context;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location currentLocation;

    public MapsCurrentLocation(MapsMVP.presenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
        buildGoogleApiClient(context);
    }

    @Override
    public Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        setCurrentLocation(location);
        presenter.updateCurrentLocation(location.getLatitude(), location.getLongitude());
        presenter.showErrorMsgIfOutOfHideAndSeekAreaBounds(location);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("connected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public boolean isUserOutSideHideAndSeekArea(Location location) {
        boolean isUserOutsideHideAndSeekArea = false;
        Circle metersAllowedToPlayIn = presenter.getMetersAllowedToPlayIn();
        if (metersAllowedToPlayIn != null) {

            double startLatitude = location.getLatitude();
            double startLongitude = location.getLongitude();
            double endLongitude = metersAllowedToPlayIn.getCenter().longitude;
            double endLatitude = metersAllowedToPlayIn.getCenter().latitude;

            float[] results = new float[2];
            Location.distanceBetween(startLatitude, startLongitude,
                    endLatitude, endLongitude, results);
            if (results[0] > metersAllowedToPlayIn.getRadius()) {
                isUserOutsideHideAndSeekArea = true;
            }
        }
        return isUserOutsideHideAndSeekArea;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        System.out.println("connection failed: " + connectionResult.getErrorMessage());
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
