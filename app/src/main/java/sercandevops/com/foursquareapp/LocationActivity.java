package sercandevops.com.foursquareapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    ListView listView;
ArrayList<String> placeName;
ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        listView = (ListView)findViewById(R.id.listview_location_activty);
        placeName = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,placeName);

        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LocationActivity.this,DetailsActivity.class);
                intent.putExtra("name",placeName.get(position));

                startActivity(intent);
            }
        });

        download();

    }

    public void download()
    {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Places");
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
                        placeName.clear();
                        for (ParseObject obj : objects)
                        {
                            placeName.add(obj.get("name").toString());
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }//UPLAODING FUNCS

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_place,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.mnu_addplace)
        {
            Intent intent = new Intent(getApplicationContext(),CreatePlaceActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.logout)
        {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null)
                    {
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }else
                    {
                        Intent intent = new Intent(LocationActivity.this,SignUp.class);
                        startActivity(intent);
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }



}
