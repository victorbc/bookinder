package equipe.projetoes.activities;

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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import org.json.JSONException;
import org.json.JSONObject;

import equipe.projetoes.AbstractGetNameTask;
import equipe.projetoes.GetNameInForeground;
import equipe.projetoes.R;
import equipe.projetoes.models.Account;
import equipe.projetoes.data.AccDAO;
import equipe.projetoes.util.Global;
import equipe.projetoes.data.RestDAO;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{


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
    private static final int RC_SIGN_IN = 9000;
    private GoogleApiClient mGoogleApiClient;




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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);


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

        final RestDAO restDAO = new RestDAO("http://192.168.25.28:8000");

        Account newAccount = new Account();
        newAccount.setEmail("stenio.araujo@ccc.ufcg.edu.br");
        newAccount.setLogin("stenio");
        newAccount.setPass("admin123");
        newAccount.setEmail_google("steniogames@hotmail.com");
        newAccount.setEmail_facebook("stenioelson@hotmail.com");

//        restDAO.createUser(newAccount, new Callback<Account>() {
//            @Override
//            public void execute(Account result) {
//                restDAO.authenticate(result.getLogin(), "admin123", new Callback<Account>() {
//                    @Override
//                    public void execute(Account result) {
//                        Log.wtf("Account_CREATED____", result.getId() + " " + result.getEmail_facebook());
//                    }
//                });
//            }
//        });

/* ISSO aqui [e apenas testando o uso do restDAO
        final Livro livro = new Livro();
        livro.setNome("Harry Potter");
        livro.setISBN("123455");
        restDAO.authenticate("stenio", "admin123", new Callback<Account>() {
            @Override
            public void execute(Account result) {
                restDAO.createLivro(livro, new Callback<Livro>() {
                    @Override
                    public void execute(Livro result) {
                        Log.wtf("LIVRO CRIADO", result.getISBN() + " " + result.getAutor());

                        restDAO.addBookToLibrary(result, new Callback<LivroUser>() {
                            @Override
                            public void execute(final LivroUser resultAdded) {
                                Log.wtf("LIVRO ADICIONADO A BIBLIOTECA", ""+resultAdded.getId());

                                restDAO.getLivroUser(resultAdded.getId(), new Callback<LivroUser>() {
                                    @Override
                                    public void execute(LivroUser resultGet) {
                                        Log.wtf("O LIVRO EH O MESMO?", new Boolean(resultGet.getId() == resultAdded.getId()).toString());

                                        resultGet.setFavorite(true);
                                        resultGet.setReadPages(300);
                                        restDAO.update(resultGet, new Callback<LivroUser>() {
                                            @Override
                                            public void execute(LivroUser result) {
                                                Log.wtf("LIVRO UPDATED!!! Agora eh FAVORITO", new Boolean(result.isFavorite()).toString());

                                                restDAO.getLibrary(new Callback<List<LivroUser>>() {
                                                    @Override
                                                    public void execute(List<LivroUser> result) {
                                                        Log.wtf("LIVROS NA BIBLIOTECA", ""+result.size());
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
*/
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
        Account acc = accDAO.getAccountByLogin(login_field.getText().toString());
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
        android.accounts.Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
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
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;

        }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Login com Google", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Global.currentAcc = new Account();
            Global.currentAcc.setEmail_google(acct.getEmail());
            Global.currentAcc.setLogin(acct.getEmail());
            Global.currentAcc.setPass(acct.getServerAuthCode());
            Global.currentAcc.setFirstTime(true);
            AccDAO dao = new AccDAO(getApplicationContext());
            dao.adiciona(Global.currentAcc);
            login();

            this.finish();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    private void handleSignInResultLogado(GoogleSignInResult result) {
        Log.d("Login com Google", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            AccDAO dao = new AccDAO(getApplicationContext());
            Global.currentAcc = dao.getAccountByLogin(acct.getEmail());
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();

            this.finish();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("LoginActivity", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResultLogado(result);
        } else {
        }
    }



}
