package com.adino.ewitness;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReportDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap map;
    private ToggleButton tbtnAuto;
    private ToggleButton tbtnTheft;
    private ToggleButton tbtnSanitation;
    private ToggleButton tbtnSocial;
    private ToggleButton tbtnEducation;
    private LayoutInflater layoutInflater;
    private boolean mapReady = false;
    private  static final String TAG = "ReportDetailsActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);*/

        tbtnAuto = (ToggleButton)findViewById(R.id.tbtn_category_auto);
        tbtnTheft = (ToggleButton)findViewById(R.id.tbtn_category_theft);
        tbtnSanitation = (ToggleButton)findViewById(R.id.tbtn_category_sanitation);
        tbtnSocial = (ToggleButton)findViewById(R.id.tbtn_category_social);
        tbtnEducation = (ToggleButton)findViewById(R.id.tbtn_category_education);

        Button btnSubmit = (Button) findViewById(R.id.btn_submit_report);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Get current date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        Log.d(TAG, formattedDate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        attachToggleStateListeners();


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        init(googleMap);
    }

    public void init(GoogleMap googleMap){

        map = googleMap;
        map.setBuildingsEnabled(true);
        // Add a marker in Gbawe and move the camera
        LatLng home = new LatLng(5.582830, -0.307473);
        // Set Ghana's boundaries
        LatLng ghana_SW = new LatLng(5.082787, -2.878441);
        LatLng ghana_NE = new LatLng(11.043421, 0.636050);
        LatLngBounds GHANA = new LatLngBounds(ghana_SW, ghana_NE);

        CameraPosition cameraPosition = new CameraPosition(home, 12, 0, 65);
        map.addMarker(new MarkerOptions().position(home).title("Home"));

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //Apply Ghana's boundary
        //map.moveCamera(CameraUpdateFactory.newLatLngBounds(GHANA, 50));
        // Constrain the camera target to the Adelaide bounds.
        //map.setLatLngBoundsForCameraTarget(GHANA);

        /*map.moveCamera(CameraUpdateFactory.newLatLng(home));
        map.animateCamera(CameraUpdateFactory.zoomTo(13));*/
    }

    private void toggleAllOthers(ToggleButton selected){
        ToggleButton[] toggleButtons = {tbtnAuto, tbtnTheft, tbtnSanitation, tbtnSocial, tbtnEducation};
        for(ToggleButton tbtn: toggleButtons){
            if(tbtn != selected){
                tbtn.setChecked(false);
            }
        }

    }

    private void attachToggleStateListeners(){
        tbtnAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tbtnAuto.setBackgroundResource(R.drawable.ic_auto_checked);
                    toggleAllOthers(tbtnAuto);
                }else {
                    tbtnAuto.setBackgroundResource(R.drawable.ic_auto_unchecked);
                }
            }
        });
        tbtnEducation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tbtnEducation.setBackgroundResource(R.drawable.ic_education_checked);
                    toggleAllOthers(tbtnEducation);
                }else {
                    tbtnEducation.setBackgroundResource(R.drawable.ic_education_unchecked);
                }
            }
        });
        tbtnSanitation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tbtnSanitation.setBackgroundResource(R.drawable.ic_sanitation_checked);
                    toggleAllOthers(tbtnSanitation);
                }else {
                    tbtnSanitation.setBackgroundResource(R.drawable.ic_sanitation_unchecked);
                }
            }
        });
        tbtnSocial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tbtnSocial.setBackgroundResource(R.drawable.ic_social_checked);
                    toggleAllOthers(tbtnSocial);
                }else {
                    tbtnSocial.setBackgroundResource(R.drawable.ic_social_unchecked);
                }
            }
        });
        tbtnTheft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tbtnTheft.setBackgroundResource(R.drawable.ic_theft_checked);
                    toggleAllOthers(tbtnTheft);
                }else {
                    tbtnTheft.setBackgroundResource(R.drawable.ic_theft_unchecked);
                }
            }
        });
    }
}
