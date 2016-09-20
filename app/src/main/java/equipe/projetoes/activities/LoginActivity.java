package equipe.projetoes.activities;

/**
 * Created by kallynnykarlla e stenio on 02/09/16.
 */
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;



//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import equipe.projetoes.AbstractGetNameTask;
import equipe.projetoes.R;
import equipe.projetoes.exceptions.BookinderException;
import equipe.projetoes.models.Account;
import equipe.projetoes.utilis.AccDAO;
import equipe.projetoes.utilis.Constants;
import equipe.projetoes.utilis.Global;

public class LoginActivity extends AppCompatActivity
        implements OnConnectionFailedListener, View.OnClickListener {

    private final String ERROR_TAG = "ERRO";
=======
public class LoginActivity extends AppCompatActivity {


>>>>>>> origin/master
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


    private GoogleApiClient mGoogleApiClient;
    private SignInButton googleButton;
    private final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Set click listeners
//        mSignInButton.setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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
        nao_tenho_conta.setOnClickListener(this);

        login_field = (EditText) findViewById(R.id.editText);
        pass_field = (EditText) findViewById(R.id.editText2);
        googleButton = (SignInButton) findViewById(R.id.sign_in_button);
        googleButton.setOnClickListener(this);

        info = (TextView)findViewById(R.id.info); //izabella
        loginButton = (LoginButton) findViewById(R.id.login_button); //izabella


        callbackManager = CallbackManager.Factory.create(); //izabella

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


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
                                    Global.currentAcc = new Account();
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

    public void loginGoogle(View v) {
        Log.v("Errr-------", "BotaoLogin");
    }

    public void createNewAccount(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;

    private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        android.accounts.Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.v("ogin, ressult", result.isSuccess() + "");
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

            } else {
                Toast.makeText(LoginActivity.this, "Erro na Autenticação.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


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
