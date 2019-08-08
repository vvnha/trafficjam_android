package com.example.nhavo.map4d123;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import vn.map4d.map4dsdk.annotations.MFMarkerOptions;
import vn.map4d.map4dsdk.camera.MFCameraUpdate;
import vn.map4d.map4dsdk.camera.MFCameraUpdateFactory;
import vn.map4d.map4dsdk.maps.LatLng;
import vn.map4d.map4dsdk.maps.MFSupportMapFragment;
import vn.map4d.map4dsdk.maps.MFSwitchMode;
import vn.map4d.map4dsdk.maps.Map4D;
import vn.map4d.map4dsdk.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private static final int REQUEST_LOCATION_CODE = 69;
    private Map4D map4D;
    private boolean mode3D = true;
    private int currentMode = 2;
    private TextView txtMode;
    LocationManager locationManager;
    ImageButton btnNo;
    List<Address> addresses;
    Geocoder geocoder;
    double longtitude, latitude;
    String streetName,cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        MFSupportMapFragment mapFragment = (MFSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3D);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setTitle(R.string.app_name);
        getToken();
        txtMode = findViewById(R.id.modeSwitch);
        onListener();
        if (!isLocationPermissionEnable()) {
            requestLocationPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        }

        btnNo = (ImageButton) findViewById(R.id.btnNofi);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*longtitude = 108.22461011;
                latitude = 16.09399451;
                streetName = "Lê Văn Duyệt";
                cityName = "Đà Nẵng";*/
                if (longtitude!= 0 && latitude!= 0){

                    //Toast.makeText(getApplicationContext(),longtitude+"",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, nofi.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("Long",longtitude);
                    bundle.putDouble("Lat",latitude);
                    bundle.putString("stName",streetName);
                    bundle.putString("ctName",cityName);
                    intent.putExtra("dulieu",bundle);
                    longtitude = 0;
                    latitude = 0;
                    streetName = "";
                    cityName = "";
                    startActivity(intent);

                }else {
                    Toast.makeText(getApplicationContext(),"Nhấn vào vị trí hiện tại trên bản đồ để lấy vị trí",Toast.LENGTH_LONG).show();
                }
            }
            //Intent intent = new Intent(MainActivity.this, nofi.class);
            //startActivity(intent);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void onListener() {
        findViewById(R.id.btnEnableLocation).setOnClickListener(this);
        findViewById(R.id.btnSetTime).setOnClickListener(this);
        findViewById(R.id.btnEnable3D).setOnClickListener(this);
        findViewById(R.id.btnSwitchMode3D).setOnClickListener(this);
    }


    private void requestLocationPermission(String[] permission) {
        ActivityCompat.requestPermissions(this, permission, REQUEST_LOCATION_CODE);
    }

    boolean isLocationPermissionEnable() { // kiểm tra phiên bản máy
        boolean isLocationPermissionenabed = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isLocationPermissionenabed = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return isLocationPermissionenabed;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_CODE: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    map4D.setMyLocationEnabled(true);
                    map4D.setOnMyLocationButtonClickListener(new Map4D.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            Toast.makeText(getApplicationContext(), "My Location button clicked", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                } else {
                    Toast.makeText(this, "Need Allow Location Permission to use Location feature", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    public void onMapReady(Map4D map4D) {
        this.map4D = map4D;
        /*LatLng latLng = new LatLng(10.771666, 106.704405);
        map4D.moveCamera(MFCameraUpdateFactory.newLatLngZoom(latLng, 13));
        map4D.addMarker(new vn.map4d.map4dsdk.annotations.MFMarkerOptions().
                title("Da Nang").
                snippet("This is a city worth living!").
                position(latLng));
        */
        map4D.setOnMyLocationButtonClickListener(new Map4D.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(getApplicationContext(), "My Location Button clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        map4D.setOnMyLocationClickListener(new Map4D.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(Location location) {
                Toast.makeText(getApplicationContext(), "My Location Icon clicked", Toast.LENGTH_SHORT).show();
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map4D.setMyLocationEnabled(true);


        this.map4D = map4D;
        map4D.enable3DMode(true);
        map4D.setMinZoomPreference(5.f);
        map4D.setMaxZoomPreference(19.f);
        map4D.setMaxNativeZoom(17.f);
        map4D.setOnMapClickListener(new Map4D.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ((TextView)findViewById(R.id.lat)).setText("Lat:    " + latLng.getLatitude());
                ((TextView)findViewById(R.id.lng)).setText("Lng:   " + latLng.getLongitude());
                /*longtitude = latLng.getLongitude();
                latitude = latLng.getLatitude();
                geocoder = new Geocoder(MainActivity.this,Locale.getDefault());
                try {
                    addresses =  geocoder.getFromLocation(latLng.getLatitude(),latLng.getLongitude(),1);

                    String address = addresses.get(0).getAddressLine(0);
                    String streets = addresses.get(0).getThoroughfare();
                    String area = addresses.get(0).getLocality();
                    String city = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalcode = addresses.get(0).getPostalCode();
                    streetName = streets;
                    cityName = city;
                    Toast.makeText(getApplicationContext(), streets, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        });

        map4D.setOnMapModeChange(new Map4D.OnMapModeChangeListener() {
            @Override
            public void onMapModeChange(boolean is3DMode) {
                Toast.makeText(getApplicationContext(), is3DMode ? "2D->3D" : "3D->2D", Toast.LENGTH_SHORT).show();
                if (is3DMode) {
                    ((Button) findViewById(R.id.btnEnable3D)).setText(R.string.mode2d);
                }
                else {
                    ((Button) findViewById(R.id.btnEnable3D)).setText(R.string.mode3d);
                }
                mode3D = is3DMode;
            }
        });




        map4D.setOnMyLocationClickListener(new Map4D.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(Location location) {
                Context context;
                longtitude = location.getLongitude();
                latitude = location.getLatitude();
                geocoder = new Geocoder(MainActivity.this,Locale.getDefault());
                    try {
                        addresses =  geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                        String address = addresses.get(0).getAddressLine(0);
                        String streets = addresses.get(0).getThoroughfare();
                        String area = addresses.get(0).getLocality();
                        String city = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalcode = addresses.get(0).getPostalCode();
                        streetName = streets;
                        cityName = city;
                        Toast.makeText(getApplicationContext(), location.getLongitude()+"_"+location.getLatitude(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                //Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                //String result = null;
                //Toast.makeText(getApplicationContext(), location.describeContents(), Toast.LENGTH_LONG).show();
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        map4D.setMyLocationEnabled(true);

    }
    public void getToken(){
        FirebaseMessaging.getInstance().subscribeToTopic("testfcm");
        String token= FirebaseInstanceId.getInstance().getToken();
        new FireBaseIDTask().execute(token);

        Toast.makeText(this,token,Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnableLocation: {
                if (map4D != null && !map4D.isMyLocationEnabled()) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        requestLocationPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                        return;
                    }
                    map4D.setMyLocationEnabled(true);
                }
                break;
            }
        }

        switch (v.getId()) {
            case R.id.btnSetTime: {
                String givenDateString = "2000-01-01";
                ((TextView) findViewById(R.id.time)).setText(givenDateString);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date mDate = sdf.parse(givenDateString);
                    map4D.setTime(mDate);
                } catch (ParseException e) {
                }
                break;
            }
            case R.id.btnEnable3D: {
                if (map4D != null) {
                    map4D.enable3DMode(!mode3D);
                }
                break;
            }
            case R.id.btnSwitchMode3D: {
                if (map4D != null) {
                    MFSwitchMode switchType;
                    ++currentMode;
                    switch (currentMode) {
                        case 1: {
                            switchType = MFSwitchMode.Auto2DTo3D;
                            Toast.makeText(getApplicationContext(), "Switch From Manual To 2D->3D", Toast.LENGTH_SHORT).show();
                            ((Button) findViewById(R.id.btnSwitchMode3D)).setText(R.string.mode3dTo2d);
                            txtMode.setText(R.string.mode2dTo3d);
                            break;
                        }
                        case 2: {
                            switchType = MFSwitchMode.Auto3DTo2D;
                            Toast.makeText(getApplicationContext(), "Switch From 2D->3D To 3D->2D", Toast.LENGTH_SHORT).show();
                            ((Button) findViewById(R.id.btnSwitchMode3D)).setText(R.string.auto);
                            txtMode.setText(R.string.mode3dTo2d);
                            break;
                        }
                        case 3: {
                            switchType = MFSwitchMode.Auto;
                            Toast.makeText(getApplicationContext(), "Switch From 3D->2D To Auto", Toast.LENGTH_SHORT).show();
                            ((Button) findViewById(R.id.btnSwitchMode3D)).setText(R.string.manual);
                            txtMode.setText(R.string.auto);
                            break;
                        }
                        case 4: {
                            switchType = MFSwitchMode.Manual;
                            Toast.makeText(getApplicationContext(), "Switch From Auto To Manual", Toast.LENGTH_SHORT).show();
                            ((Button) findViewById(R.id.btnSwitchMode3D)).setText(R.string.mode2dTo3d);
                            txtMode.setText(R.string.manual);
                            currentMode = 0;
                            break;
                        }
                        default: {
                            switchType = MFSwitchMode.Default;
                            Toast.makeText(getApplicationContext(), "Current", Toast.LENGTH_SHORT).show();
                            ((Button) findViewById(R.id.btnSwitchMode3D)).setText(R.string.mode2dTo3d);
                            txtMode.setText("Default");
                            currentMode = 0;
                            break;
                        }
                    }
                    map4D.setSwitchMode(switchType);
                }
                break;
            }
        }

    }
}
