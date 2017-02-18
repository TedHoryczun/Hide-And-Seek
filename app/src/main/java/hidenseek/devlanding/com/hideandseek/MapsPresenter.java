package hidenseek.devlanding.com.hideandseek;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ted on 2/17/17.
 */

public class MapsPresenter implements MapsMVP.presenter{
    private final MapsMVP.view view;
    private final MapsInteractor interactor;
    private LocationRequest mLocationRequest;

    public MapsPresenter(MapsMVP.view view, Context context){
        this.view = view;
        this.interactor = new MapsInteractor();
    }
    @Override
    public Location getCurrentLocation() {
        return interactor.getCurrentLocation();
    }
    @Override
    public void getLocationUpdatesEveryMillisecond(Context context, int millisecond){
        interactor.buildGoogleApiClient(context);
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(getCurrentLocation() != null){
                   view.createMarketAtCurrentLocation(getCurrentLocation());
                }
            }
        }, 0, 20, TimeUnit.SECONDS);
    }

}
