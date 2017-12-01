package com.adino.ewitness;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UdacityMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;

    /**
     * Id to identity LOCATION_SERVICE permission request.
     */
    private static final int REQUEST_LOCATION_SERVICE = 0;

    private Button btnSatellite;
    private View fragMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udacity_map);

        btnSatellite = (Button) findViewById(R.id.btnSatellite);
        fragMap = findViewById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        init(googleMap);
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean locationEnabled = false;
        // get Location setting
        try{
            locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){}

        if(!locationEnabled){
            Snackbar.make(fragMap, R.string.location_permission_prompt, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
        }

        Snackbar.make(btnSatellite, "Snackbar has been created", Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }

    public boolean mayAccessLocation(){

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(LOCATION_SERVICE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(LOCATION_SERVICE)) {
            Snackbar.make(btnSatellite, R.string.location_permission_prompt, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{LOCATION_SERVICE}, REQUEST_LOCATION_SERVICE);
                        }
                    });
        }else {
            requestPermissions(new String[]{LOCATION_SERVICE}, REQUEST_LOCATION_SERVICE);
        }

        return false;
    }

    public void init(GoogleMap googleMap){
        map = googleMap;

        // Add a marker in Gbawe and move the camera
        LatLng home = new LatLng(5.582830, -0.307473);
        map.addMarker(new MarkerOptions().position(home).title("Home"));
        map.moveCamera(CameraUpdateFactory.newLatLng(home));
        map.animateCamera(CameraUpdateFactory.zoomTo(13));
    }
}
