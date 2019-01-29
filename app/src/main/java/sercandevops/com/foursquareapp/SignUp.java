package sercandevops.com.foursquareapp;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity {


    private EditText ed_username;
   private EditText ed_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ed_username = findViewById(R.id.ed_username_signup);
        ed_password = findViewById(R.id.ed_password_signup);

        ParseUser parseUser = ParseUser.getCurrentUser();

        if(parseUser != null)
        {
            Intent intent = new Intent(SignUp.this,LocationActivity.class);
            startActivity(intent);
        }

    }

    public void SignIn(View view) {

        String username = ed_username.getText().toString().trim();
        String password = ed_password.getText().toString().trim();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null)
                {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"başarılı kayıt",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this,LocationActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void signUp(View view) {

        String username = ed_username.getText().toString().trim();
        String password = ed_password.getText().toString().trim();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null)
                {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Giriş Başarılı",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this,LocationActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
