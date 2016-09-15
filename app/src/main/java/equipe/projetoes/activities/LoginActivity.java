package equipe.projetoes.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthUtil;

import org.json.JSONException;
import org.json.JSONObject;

import equipe.projetoes.AbstractGetNameTask;
import equipe.projetoes.GetNameInForeground;
import equipe.projetoes.R;
import equipe.projetoes.utilis.AccDAO;
import equipe.projetoes.utilis.Global;


public class LoginActivity extends AppCompatActivity {


    Context mContext = LoginActivity.this;
    AccountManager mAccountManager;
    TextView nao_tenho_conta;
    EditText login_field;
    EditText pass_field;
    String token;
    int serverCode;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    AccDAO accDAO;
    private TextView info; //izabella
    private LoginButton loginButton;//izabella
    private CallbackManager callbackManager;//izabella
    private AccessTokenTracker accessTokenTracker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        updateWithToken(AccessToken.getCurrentAccessToken());

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

//        syncGoogleAccount();

        accDAO = new AccDAO(this);
        TextView nao_tenho_conta = (TextView) findViewById(R.id.nao_tenho_conta);
//        nao_tenho_conta.setOnClickListener(this);

        login_field = (EditText) findViewById(R.id.editText);
        pass_field = (EditText) findViewById(R.id.editText2);


        info = (TextView)findViewById(R.id.info); //izabella
        loginButton = (LoginButton) findViewById(R.id.login_button); //izabella


        callbackManager = CallbackManager.Factory.create(); //izabella

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        loginButton.setReadPermissions("email");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fblogin();
            }
        });
    }

    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);

                    finish();
                }
            }, 0);
        }
    }

    private void fblogin() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        { //izabella ate o fim


            @Override
            public void onSuccess (LoginResult loginResult){
//                Toast.makeText(LoginActivity.this, "clicou teste", Toast.LENGTH_LONG).show();
               // info.setText(
               //         "User ID: "
               //                 + loginResult.getAccessToken().getUserId()
               //                 + "\n" +
               //                 "Auth Token: "
               //                 + loginResult.getAccessToken().getToken()
               // );
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                Log.i("json", me.toString());
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    Global.currentAcc = new equipe.projetoes.models.Account();
                                    String email;
                                    try {
                                        email = response.getJSONObject().get("email").toString();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Global.currentAcc.setEmail_facebook(me.optString("email"));
                                    Global.currentAcc.setEmail(me.optString("email"));
                                    Global.currentAcc.setLogin(me.optString("name"));
                                    Global.currentAcc.setPass(String.valueOf(me.hashCode()));
                                    AccDAO dao = new AccDAO(getApplicationContext());
                                    dao.adiciona(Global.currentAcc);
                                    Log.i("token", AccessToken.getCurrentAccessToken().toString());
                                    // send email and id to your web server
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
                login();
            }

            @Override
            public void onCancel () {
               // info.setText("Login attempt canceled.");

            }

            @Override
            public void onError (FacebookException e){
                Toast.makeText(LoginActivity.this, "Login attempt failed.", Toast.LENGTH_LONG).show();

            }
        });
    }



//    @Override
//    public void onClick(View v) {
//        String email = email_field.getText().toString();
//
//        switch (v.getId()) {
//            case R.id.nao_tenho_conta:
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                intent.putExtra(Constants.TAG_EMAIL, email);
//                startActivity(intent);
//                finish();
//                break;
//
//        }
//    }

    public void login(View v) {
        equipe.projetoes.models.Account acc = accDAO.getAccountByLogin(login_field.getText().toString());
        if (acc != null && acc.getLogin() != null && !acc.getLogin().equals("") && acc.getPass().equals(pass_field.getText().toString())) {
            Global.currentAcc = acc;

            if (acc.isFirstTime())
                startActivity(new Intent(LoginActivity.this, CategoriasActivity.class));
            else
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            finish();
        } else {

            Toast.makeText(this, "Login Invalid", Toast.LENGTH_SHORT).show();
        }

    }

    public void createNewAccount(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }


    private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

    private AbstractGetNameTask getTask(LoginActivity activity, String email, String scope) {
        return new GetNameInForeground(activity, email, scope);
    }

    private void syncGoogleAccount() {
        if (isNetworkAvailable() == true) {
            String[] accountarrs = getAccountNames();
            if (accountarrs.length > 0) {
                getTask(LoginActivity.this, accountarrs[0], SCOPE).execute();
            } else {
                Toast.makeText(LoginActivity.this, "No google Account Sync!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "No Network Services!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            Log.e("Network Testing", "Available");
            return true;
        }
        Log.e("Network Testing", "Not Available");
        return false;
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
