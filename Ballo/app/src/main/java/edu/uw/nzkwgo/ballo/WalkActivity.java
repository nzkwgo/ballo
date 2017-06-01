package edu.uw.nzkwgo.ballo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class WalkActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, Ballo.Events {

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOC_REQUEST_CODE = 0;
    private int mSelectedColor = -16777216;
    private Polyline currentLine;
    private List<Polyline> polylines = new ArrayList<Polyline>();
    private boolean isPenning = false;
    private double dist;
    private Ballo ballo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        setContentView(R.layout.activity_walk);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Wire the action button
        findViewById(R.id.walkEndButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WalkActivity.this, "Completed your walk!", Toast.LENGTH_SHORT)
                        .show();
                startActivity(new Intent(WalkActivity.this, HomeActivity.class));
            }
        });

        dist = 0;

        ballo = Ballo.getBallo(this);
        ballo.setEventHandler(this);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
//        ballo.walk(dist);
//        Ballo.saveBallo(this, ballo);
        Log.v("WALK", "ON PAUSE");
        ballo.walk(1);
        Ballo.saveBallo(this, ballo);
        dist = 0;
        super.onPause();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);

            mMap.setMyLocationEnabled(true);


            if (mLastLocation != null) {
                    LatLng lastPosition = getLatLng(mLastLocation);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lastPosition));
                }

                startLocationUpdates();
        } else {
            requestPermission();
        }
    }

    private LatLng getLatLng(Location loc) {
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            // Granted permission, continue
            case LOC_REQUEST_CODE:
                onConnected(null);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(getLatLng(location)));
        double currDist = distance(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                location.getLatitude(), location.getLongitude());
        dist += currDist;
//        Toast.makeText(this, "Distance = " + dist, Toast.LENGTH_SHORT).show();
        Log.v("WALK", "Distance = " + dist);

        // trying updating strength everytime location changes
        ballo.walk(currDist);
        Ballo.saveBallo(this, ballo);

        mLastLocation = location;
        if (isPenning) {
            List<LatLng> polyPoints = currentLine.getPoints();
            polyPoints.add(getLatLng(location));
            currentLine.setPoints(polyPoints);
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist2 = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist2 = Math.acos(dist2);
        dist2 = rad2deg(dist2);
        dist2 = dist2 * 60 * 1.1515 * 1000; // * 1000 changes it to be in meters
        return (dist2);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    protected void startLocationUpdates() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ballo.destroy();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
         mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
    }

    public void newPolyline() {
        PolylineOptions temp = new PolylineOptions()
                .color(mSelectedColor)
                .add(getLatLng(mLastLocation));
        currentLine = mMap.addPolyline(temp);
    }

    public void endPolyline() {
        polylines.add(currentLine);
    }

    @Override
    public void onUpdate() {
        Ballo.saveBallo(this, ballo);
    }
}
