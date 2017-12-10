package com.adino.ewitness;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReportDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback{
    private GoogleMap mMap;
    private ToggleButton tbtnAuto;
    private ToggleButton tbtnTheft;
    private ToggleButton tbtnSanitation;
    private ToggleButton tbtnSocial;
    private ToggleButton tbtnEducation;
    private LayoutInflater layoutInflater;
    private ImageView imageView;
    private EditText txtCaption;
    private Button btnSubmit;
    private boolean mapReady = false;
    private static final String TAG = "ReportDetailsActivity";
    public static final int DEFAULT_CAPTION_LENGTH_LIMIT = 1000;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 111;

    private boolean mLocationPermissionsGranted = false;
    private ToggleButton selectedTbtn;
    private String mCurrentPhotoPath;
    private byte[] photo;

    /**
     * Report variables
     */
    private ReportCategory category;
    private  String caption;
    private String date;
    private String imageURL = "";
    private String videoURL = "";
    private String location = "";

    /**
     * Firebase variables
     */
    private FirebaseStorage mFirebaseStorage;
    private ChildEventListener childEventListener;
    private StorageReference mPhotosStorageReference;
    private UploadTask uploadTask;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference messagesDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getLocationPermission();


        tbtnAuto = (ToggleButton)findViewById(R.id.tbtn_category_auto);
        tbtnTheft = (ToggleButton)findViewById(R.id.tbtn_category_theft);
        tbtnSanitation = (ToggleButton)findViewById(R.id.tbtn_category_sanitation);
        tbtnSocial = (ToggleButton)findViewById(R.id.tbtn_category_social);
        tbtnEducation = (ToggleButton)findViewById(R.id.tbtn_category_education);
        txtCaption = (EditText)findViewById(R.id.txtCaption);
        btnSubmit = (Button) findViewById(R.id.btn_submit_report);
        btnSubmit.setEnabled(false);

        mFirebaseStorage = FirebaseStorage.getInstance();
        mPhotosStorageReference = mFirebaseStorage.getReference().child("photos");
        firebaseDatabase = FirebaseDatabase.getInstance();
        messagesDatabaseReference = firebaseDatabase.getReference().child("reports");
        //attach child event listener
        attachDatabaseReadListener();


        ImageView imageView = (ImageView) findViewById(R.id.image_view_report_image);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        photo = byteArray;
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageView.setImageBitmap(imageBitmap);



        // Enable Send button when there's text to send
        txtCaption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnSubmit.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    btnSubmit.setEnabled(true);
                } else {
                    btnSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        txtCaption.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_CAPTION_LENGTH_LIMIT)});

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Submit button clicked");
                uploadImage();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        attachToggleStateListeners();
        //Set Automobile by default
        tbtnAuto.setChecked(true);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapReady = true;
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mLocationPermissionsGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0){
                    for(int g: grantResults) {
                        if (g != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    mLocationPermissionsGranted = true;
                }
        }
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                //initMap();
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

        }
    }

    public void initMap(){
        SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        supportMapFragment.getMapAsync(ReportDetailsActivity.this);

        /*map = googleMap;
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

    /**
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.ENGLISH).format(new Date());
        String imageFileName = "PHOTO_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",      /* suffix */
                storageDir         /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     *
     */
    private void uploadImage(){
        try {
            File imageFile = createImageFile();
            Uri file = Uri.fromFile(imageFile);
            StorageReference photoRef = mPhotosStorageReference.child(file.getLastPathSegment());

            // Create file metadata including the content type
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();
            uploadTask = photoRef.putBytes(photo, metadata);

        }catch (IOException e){
            e.printStackTrace();
        }

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                imageURL = downloadUrl.toString();

                category = getSelectedCategory(selectedTbtn);
                date = getCurrentDate();
                caption = getCaption();
                Report report = new Report(caption, date, category, imageURL, location);

                messagesDatabaseReference.push().setValue(report);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(btnSubmit, "Image uploaded successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Get the name of the image file to store in database
     * @param path Path of the image file
     * @return Return the name of the image file
     */
    public String getImageURI(String path) {
        int index = path.lastIndexOf("/") + 1;
        return path.substring(index);
    }

    /**
     * Toggle all other toggle buttons as unselected when one is selected
     * This method ensures that only one toggle button is selected
     * @param selected
     */
    private void toggleAllOthers(ToggleButton selected){
        Log.d(TAG, "toggleAllOthers: called");
        selectedTbtn = selected;
        ToggleButton[] toggleButtons = {tbtnAuto, tbtnTheft, tbtnSanitation, tbtnSocial, tbtnEducation};
        for(ToggleButton tbtn: toggleButtons){
            if(tbtn != selected){
                tbtn.setChecked(false);
            }
        }

    }

    /**
     *
     */
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

    /**
     *
     * @param selectedTbtn
     * @return
     */
    private ReportCategory getSelectedCategory(ToggleButton selectedTbtn){
        ReportCategory reportCategory = null;
        if(selectedTbtn != null){
            if(selectedTbtn == tbtnAuto){
                reportCategory = ReportCategory.AUTOMOBILE_OFFENCE;
            }else if(selectedTbtn == tbtnEducation){
                reportCategory = ReportCategory.EDUCATIONAL_MISCONDUCT;
            }else if(selectedTbtn == tbtnSanitation){
                reportCategory = ReportCategory.SANITATION_MISCONDUCT;
            }else if(selectedTbtn == tbtnTheft){
                reportCategory = ReportCategory.THEFT;
            }else if(selectedTbtn == tbtnSocial){
                reportCategory = ReportCategory.SOCIAL_MISCONDUCT;
            }
        }
        return reportCategory;
    }

    /**
     *
     * @return
     */
    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return df.format(c.getTime());
    }

    /**
     *
     * @return
     */
    private String getCaption(){
        return txtCaption.getText().toString();
    }

    /**
     *
     * @return
     */
    private String getLocation(){
        return "";
    }

    /**
     *
     */
    private void attachDatabaseReadListener(){
        if(childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    //mMessageAdapter.add(friendlyMessage);
                    //go back to home page if report is submitted successfully
                    Snackbar.make(btnSubmit, "Report submitted successfully!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent home = new Intent(ReportDetailsActivity.this, MainActivity.class);
                    startActivity(home);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            messagesDatabaseReference.addChildEventListener(childEventListener);
        }
    }

    /**
     *
     */
    private void removeDatabaseReadListener(){
        if(childEventListener != null) {
            messagesDatabaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }
}
