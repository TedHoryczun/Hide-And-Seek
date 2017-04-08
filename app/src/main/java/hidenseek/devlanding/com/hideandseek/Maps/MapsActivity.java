package hidenseek.devlanding.com.hideandseek.Maps;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import hidenseek.devlanding.com.hideandseek.HideAndSeekArea;
import hidenseek.devlanding.com.hideandseek.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MapsMVP.view, HideAndSeekArea.OnFragmentInteractionListener {

    private static final int MY_PERMISSIONS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private MapsPresenter presenter;
    private Circle metersAllowedToPlayIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapsFragment);

        presenter = new MapsPresenter(this, getApplicationContext());
        mapFragment.getMapAsync(this);
        presenter.displayPopupWouldYouLikeToPlayOrCreateGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startFirebase();
        presenter.loginToFirebase();
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
        }
    }


    @Override
    public void askForCurrentLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_FINE_LOCATION);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        presenter.stopFirebase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void addCircleAroundCurrentLocationWhereAllowedToPlay(LatLng currentLocationOnMap, Integer meters) {
        int strokeColor = 0xffff0000;
        int shadeColor = 0x44ff0000;
        CircleOptions circleOptions = new CircleOptions().center(currentLocationOnMap).radius(meters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);
        presenter.metersAllowedToPlayIn = mMap.addCircle(circleOptions);
        presenter.metersAllowedToPlayInAlreadyDrawn = true;
    }

    @Override
    public void updateCircleAroundCurrentLocationWhereAllowedToPlay(LatLng currentLocationOnMap, Integer meters) {
        presenter.metersAllowedToPlayIn.setCenter(currentLocationOnMap);
        presenter.metersAllowedToPlayIn.setRadius(meters);
    }

    @Override
    public void displayErrorMsgIfOutOfHideAndSeekAreaBounds(Location location) {
        if (presenter.isUserOutSideHideAndSeekArea(location)) {
            displayErrorOutOfBounds(location);
        }

    }

    @Override
    public void displayErrorOutOfBounds(final Location currentLocation) {
        presenter.outOfBoundsErrorCurrentlyBeingDisplayed = true;
        final AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this)
                .setTitle(getResources().getString(R.string.outOfBoundsTitle))
                .setMessage(getResources().getString(R.string.outOfBoundsMsg))
                .setPositiveButton("I'm Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (presenter.isUserOutSideHideAndSeekArea(currentLocation)) {
                            dialogInterface.cancel();
                        }
                        presenter.outOfBoundsErrorCurrentlyBeingDisplayed = false;
                    }
                }).show();

    }

    @Override
    public void displayAlertDialogWouldYouLikeToPlayOrCreateAGame() {

        AlertDialog alertDialogBuilder = new AlertDialog.Builder(
                MapsActivity.this)
                .setTitle("Create A Game")
                .setMessage("Would you like to create, or play a game?")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.createGame();
                    }
                })
                .setNegativeButton("Play", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.playGame();
                    }
                }).show();
    }

    @Override
    public void displayMapSeekingAreaSelector() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mapsAreaSelector, HideAndSeekArea.newInstance("", ""))
                .commit();
    }

    @Override
    public void displayEnterGameCodeDialog() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Game Code")
                .setMessage("Enter Hide and Seek Game Code")
                .setView(taskEditText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String gameCode = taskEditText.getText().toString();
                        presenter.enterGame(gameCode);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();


    }

    @Override
    public void displayNoGameExistsError(String gameCode) {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Game Code")
                .setMessage(gameCode + " Doesn't Exist, Enter Hide and Seek Game Code")
                .setView(taskEditText)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String gameCode = taskEditText.getText().toString();
                        presenter.enterGame(gameCode);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    @Subscribe
    public void addMetersAllowedToHideAroundCurrentLocationOnMap(Integer meters) {
        Location location = presenter.getCurrentLocation();
        LatLng latlngCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        presenter.addMetersAllowedToPlay(latlngCurrentLocation, meters);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.leaveAllGames();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        presenter.loginToFirebase();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }
}
