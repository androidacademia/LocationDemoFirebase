package login.com.girish.locationdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {
    private TextView textViewLoc;
    private LocationManager locationManager;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            long curTime = location.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(curTime);
            String c_date = calendar.get(Calendar.DAY_OF_MONTH)
                    +"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR)+"" +
                    " :"+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);;

            //float speed = location.getSpeedAccuracyMetersPerSecond();
            float speed = location.getSpeed();
            textViewLoc.setText(lat + "," + lng+"\n Speed = "+speed);
            /////////////////////////FIREBASE///////////////////////
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("location");
            MyLocation myLocation = new MyLocation();
            myLocation.setLat(""+lat);
            myLocation.setLng(""+lng);
            myLocation.setLocDate(c_date);
            myRef.push().setValue(myLocation);
            ///////////////////////////////////////////////////////
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private final int MY_REQUEST_CODE = 122;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewLoc = findViewById(R.id.textView);

        //FirebaseDatabase database = FirebaseDatabase.getInstance("user/admin/name");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        //DatabaseReference childREF = myRef.child("user/admin/name");
        DatabaseReference childREF = myRef.child("user/admin/name").child("admin").child("name");

        childREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();//the value i.e. girish...
                dataSnapshot.getKey();// key i.e. name
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    public void fetchLocation(View view) {
        //1. create an object of LocationManager....
        textViewLoc.setText("wait fetching location....");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2. fetch location once using method name getLastKnownLocation();
        /*
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //3. start fetching location using methods available in Location Class...
        if (location!=null){
            //use here getter methods to fetch location
            // like getLatitude() and getLongitute();
            // double.....
        }*/

        //4. fetch location automatically based on provider,distance, time......
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mBuilder.setTitle("Location Permission");
                mBuilder.setMessage("Any way you need to permit...");
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_REQUEST_CODE);
                    }
                });
                mBuilder.setNegativeButton("Cancel",null);
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_REQUEST_CODE);
            }

        }else{
            showLocation();
        }



    }

    public void showLocation(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    //fetch the location
                    showLocation();
                }else {
                    Toast.makeText(this, "Permission required..", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void showLocations(View view) {
        startActivity(new Intent(this,MyListActivity.class));


    }
}















