package sercandevops.com.foursquareapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {


SupportMapFragment mapFragment;
GoogleMap mMap;

TextView nameText,typeText,amtosText;
ImageView imageView;
String placeName;
String latitudeString;
String longitudeString;
Double latitude;
Double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        nameText = findViewById(R.id.tv_Name);
        typeText = findViewById(R.id.tv_type);
        amtosText = findViewById(R.id.tv_atmoshape);
        imageView = findViewById(R.id.imageView);

        Intent intent = this.getIntent();
        placeName = intent.getStringExtra("name");



    mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapDetail);
    mapFragment.getMapAsync(this);

        getData();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
    }

    public void getData()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
        query.whereEqualTo("name",placeName);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e != null)
                {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }else
                {
                    if(objects.size() > 0)
                    {
                        for (ParseObject ob : objects)
                        {
                            ParseFile parseFile = (ParseFile)ob.get("image");
                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e == null && data != null)
                                    {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                        imageView.setImageBitmap(bitmap);
                                    }
                                }
                            });//IMAGE DOWNLAD

                            nameText.setText(placeName);
                            typeText.setText(ob.getString("type"));
                            amtosText.setText(ob.getString("atmos"));

                            latitudeString = ob.getString("latitude");
                            longitudeString = ob.getString("longitude");

                            latitude = Double.parseDouble(latitudeString);
                            longitude = Double.parseDouble(longitudeString);

                            mMap.clear();

                            LatLng placeLocation = new LatLng(latitude,longitude);
                            mMap.addMarker(new MarkerOptions().position(placeLocation).title(placeName));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation,15));
                        }
                    }
                }
            }
        });
    }//GETDAATA


}
