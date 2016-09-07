package equipe.projetoes.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import equipe.projetoes.R;


public class LoginActivity extends AppCompatActivity {

    private TextView info; //izabella
    private LoginButton loginButton;//izabella
    private CallbackManager callbackManager;//izabella


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        info = (TextView)findViewById(R.id.info); //izabella
        loginButton = (LoginButton) findViewById(R.id.login_button); //izabella


        callbackManager = CallbackManager.Factory.create(); //izabella

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //loginButton.setReadPermissions("user_friends", "email");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fblogin();
            }
        });
    }

    private void fblogin() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        { //izabella ate o fim


            @Override
            public void onSuccess (LoginResult loginResult){
                Toast.makeText(LoginActivity.this, "clicou teste", Toast.LENGTH_LONG).show();
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
                login();


            }

            @Override
            public void onCancel () {
                info.setText("Login attempt canceled.");

            }

            @Override
            public void onError (FacebookException e){
                info.setText("Login attempt failed.");

            }

        });
    }

    public void login(){
        startActivity(new Intent(LoginActivity.this,CategoriasActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //izabella
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }








}
