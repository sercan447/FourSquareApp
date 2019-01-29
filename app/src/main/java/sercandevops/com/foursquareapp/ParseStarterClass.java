package sercandevops.com.foursquareapp;

import android.app.Application;

import com.parse.Parse;

public class ParseStarterClass extends Application {


    @Override
    public void onCreate() {
        super.onCreate();


        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("qAKhKix15yU3w8Ym8dsDVgMmrmQDa68icvbB09dS")
        .clientKey("hpLluWX53LBQUqcnrbGICF72rd29LRZdXaYUor4U")
        .server("https://parseapi.back4app.com/")
        .build());
    }



}
