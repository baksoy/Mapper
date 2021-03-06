package com.thinkful.mapper;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.List;


public class MapsActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        showLocation(mCurrentLocation);
        startLocationUpdates();
    }

    public void onLocationChanged(Location location){
        showLocation(location);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onPause(){
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mGoogleApiClient.isConnected()){
            setUpMapIfNeeded();
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

//        LatLng latLng = new LatLng(40.72493, -73.996599);
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Thinkful Headquarters");
//        markerOptions.snippet("On a mission to reinvent education");
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.thinkful));
//
//        mMap.addMarker(markerOptions).showInfoWindow();
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(19),2000,null);
//            }
//        }, 2000);

        mMap.setMyLocationEnabled(true);

    }

    protected void showLocation(Location mCurrentLocation){
        if(mCurrentLocation != null){
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21));
//          mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        Log.i("Where am I", "Latitude : " + mCurrentLocation.getLatitude() + " Longtitude : " + mCurrentLocation.getLongitude());

        if(mLastLocation != null){
            PolylineOptions polyLineOptions = new PolylineOptions()
                        .add(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                        .add(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            mMap.addPolyline(polyLineOptions);
        }
        mLastLocation = mCurrentLocation;
    }

    protected void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

}
