package com.adino.ewitness;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReportDetailsActivity extends AppCompatActivity {
    private ToggleButton tbtnAuto;
    private ToggleButton tbtnTheft;
    private ToggleButton tbtnSanitation;
    private ToggleButton tbtnSocial;
    private ToggleButton tbtnEducation;
    private  static final String TAG = "ReportDetailsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tbtnAuto = (ToggleButton)findViewById(R.id.tbtn_category_auto);
        tbtnTheft = (ToggleButton)findViewById(R.id.tbtn_category_theft);
        tbtnSanitation = (ToggleButton)findViewById(R.id.tbtn_category_sanitation);
        tbtnSocial = (ToggleButton)findViewById(R.id.tbtn_category_social);
        tbtnEducation = (ToggleButton)findViewById(R.id.tbtn_category_education);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_submit_report);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Get current date
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String formattedDate = df.format(c.getTime());
        Log.d(TAG, formattedDate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        attachToggleStateListeners();

        ToggleButton[] toggleButtons = {tbtnAuto, tbtnTheft, tbtnSanitation, tbtnSocial};


    }

    private void toggleAllOthers(){

    }

    private void attachToggleStateListeners(){
        tbtnAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tbtnAuto.setBackgroundResource(R.drawable.ic_auto_checked);
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
                }else {
                    tbtnTheft.setBackgroundResource(R.drawable.ic_theft_unchecked);
                }
            }
        });
    }
}
