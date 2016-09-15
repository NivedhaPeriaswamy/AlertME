package com.example.nivi.alertme;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity implements LocationListener{

    TextView source_gn;
    TextView dest_gn;
    TextView cur_loc;
    TextView distance;
    TextView time;
    SharedPreferences sp;
    Button view_map;
    //Button sign_out;
    protected LocationManager locationManager;
    Location location;
    String source;
    String dest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        source_gn = (TextView)findViewById(R.id.source_gn);
        dest_gn = (TextView)findViewById(R.id.dest_gn);
        cur_loc = (TextView)findViewById(R.id.cur_loc_gn);
        distance = (TextView)findViewById(R.id.remain_dist_calc);
        time = (TextView)findViewById(R.id.time_calc);
        view_map = (Button)findViewById(R.id.button2);
       // sign_out = (Button)findViewById(R.id.button3);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        source = sp.getString("source", " h");
        dest = sp.getString("destination", "h ");
        source_gn.setText(source);
        dest_gn.setText(dest);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        view_map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapViewActivity.class));
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        double sor_lat = location.getLatitude();
        double sor_long = location.getLongitude();
        Log.d("location", sor_lat+ " " + sor_long);
        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), this, new GeocoderHandler());

        //cal_distance(dest);
        GeocodingLocation.getAddressFromLocation(dest, getApplicationContext(), new Handler());
        double dest_lat = Double.longBitsToDouble(sp.getLong("latitude", 0));
        double dest_long = Double.longBitsToDouble(sp.getLong("longitude", 0));

        cur_loc.setText(sp.getString("location", " "));
        Location locationA = new Location("point A");

        locationA.setLatitude(sor_lat);
        locationA.setLongitude(sor_long);

        Location locationB = new Location("point B");

        locationB.setLatitude(dest_lat);
        locationB.setLongitude(dest_long);

        float distance_cal = locationA.distanceTo(locationB)/1000;

        distance.setText(distance_cal+"kms");

        float speed = 60;

        float time_cal = distance_cal/speed;

        time.setText(time_cal+"hrs");



    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void cal_distance(String dest){

    }

}
class GeocoderHandler extends Handler {
    @Override
    public void handleMessage(Message message) {
        String locationAddress;
        switch (message.what) {
            case 1:
                Bundle bundle = message.getData();
                locationAddress = bundle.getString("address");
                break;
            default:
                locationAddress = null;
        }
        //location.setText(locationAddress);
        Log.d("Address", "Current Address : " + locationAddress.toString());
    }
}