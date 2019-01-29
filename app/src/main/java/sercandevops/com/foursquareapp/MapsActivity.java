package sercandevops.com.foursquareapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    String latitudeString;
    String longitudeString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //degişen lkocation

                SharedPreferences sharedPreferences = getSharedPreferences("sercandevops.com.foursquareapp",MODE_PRIVATE);
               boolean firstTimeCheck = sharedPreferences.getBoolean("notFirstime",false);

               if(!firstTimeCheck)
               {
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.putBoolean("notFirstime",true);
                   editor.apply();
                   editor.commit();

                   LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
               }

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
        };

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else
        {
            //get location
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0 ,locationListener);
            mMap.clear();

            Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnowLocation != null)
            {
                LatLng lastUserLocation = new LatLng(lastKnowLocation.getLatitude(),lastKnowLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));

            }
        }

    }//func


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1)
        {
            if(grantResults.length > 0)
            {
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {

                    //get location
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0 ,locationListener);
                    mMap.clear();

                    Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnowLocation != null)
                    {
                        LatLng lastUserLocation = new LatLng(lastKnowLocation.getLatitude(),lastKnowLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));

                    }

                }
            }
        }
    }//FNC

    @Override
    public void onMapLongClick(LatLng latLng) {

        Double lati = latLng.latitude;
        Double latu = latLng.longitude;

        latitudeString = lati.toString();
        longitudeString = latu.toString();

        mMap.addMarker(new MarkerOptions().title("New place").position(latLng));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_place,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save_place)
        {
            Uploading();
            Intent intent = new Intent(getApplicationContext(),LocationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void Uploading()
    {
        PlacesClass placesClass = PlacesClass.getInstance();

        String placeName = placesClass.getName();
        String placeType = placesClass.getType();
        String placeAtmos = placesClass.getAtmosphare();
        Bitmap placeImage = placesClass.getImage();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        placeImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ParseFile parseFile = new ParseFile("imagae.png",bytes);

        ParseObject parseObject = new ParseObject("Places");
        parseObject.put("image",parseFile);
        parseObject.put("name",placeName);
        parseObject.put("type",placeType);
        parseObject.put("atmos",placeAtmos);
        parseObject.put("username",ParseUser.getCurrentUser().getUsername());
        parseObject.put("latitude",latitudeString);
        parseObject.put("longitude",longitudeString);

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if( e != null)
                {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Başarılı Kayıt",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
