package hidenseek.devlanding.com.hideandseek.Maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import hidenseek.devlanding.com.hideandseek.HideAndSeekArea;
import hidenseek.devlanding.com.hideandseek.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsMVP.view {

    private static final int MY_PERMISSIONS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private MapsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapsFragment);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mapsAreaSelector, HideAndSeekArea.newInstance("", ""))
                .commit();
        presenter = new MapsPresenter(this, getApplicationContext());
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    finish();
                }
                return;
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            presenter.askToReceiveCurrentLocationPermission();
            return;
        } else {
            mMap.setMyLocationEnabled(true);
            presenter.getLocationUpdatesEveryMillisecond(getApplicationContext(), 1000);
        }
    }

    @Override
    public void createMarketAtCurrentLocation(Location currentLocation) {
    }

    @Override
    public void askForCurrentLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_FINE_LOCATION);
    }
}
