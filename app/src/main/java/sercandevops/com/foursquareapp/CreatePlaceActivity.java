package sercandevops.com.foursquareapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class CreatePlaceActivity extends AppCompatActivity {

    EditText ed_place_name,ed_place_type,ed_atmshape;
    ImageView img_place_Image;
    Bitmap chooseImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);

        ed_place_name = findViewById(R.id.ed_place_name);
        ed_place_type = findViewById(R.id.ed_place_type);
        ed_atmshape= findViewById(R.id.ed_atmshape);
        img_place_Image = findViewById(R.id.img_place_Image);


    }

    public void btnPlaceNext(View view) {

        PlacesClass placesClass = PlacesClass.getInstance();

        String placeName = ed_place_name.getText().toString().trim();
        String placeType = ed_place_type.getText().toString().trim();
        String placeAtmnos = ed_atmshape.getText().toString().trim();

        placesClass.setName(placeName);
        placesClass.setType(placeType);
        placesClass.setAtmosphare(placeAtmnos);
        placesClass.setImage(chooseImage);


        //intent
            Intent intent = new Intent(CreatePlaceActivity.this,MapsActivity.class);
            startActivity(intent);

    }

    public void selectedImage(View view) {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 2)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            try {
                chooseImage =   MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                img_place_Image.setImageBitmap(chooseImage);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
