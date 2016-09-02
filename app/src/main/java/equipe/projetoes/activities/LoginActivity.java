package equipe.projetoes.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;

import equipe.projetoes.AbstractGetNameTask;
import equipe.projetoes.GetNameInForeground;
import equipe.projetoes.R;

/**
 * Created by Victor on 3/30/2016.
 */
public class LoginActivity extends AppCompatActivity {

    Context mContext = LoginActivity.this;
    AccountManager mAccountManager;
    String token;
    int serverCode;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        syncGoogleAccount();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }



    public void login(View v){
        startActivity(new Intent(LoginActivity.this, CategoriasActivity.class));
        finish();
    }

    private String[] getAccountNames(){
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for(int i = 0; i < names.length; i++){
            names[i] = accounts[i].name;
        }
        return names;
    }

    private AbstractGetNameTask getTask(LoginActivity activity, String email, String scope){
        return new GetNameInForeground(activity,email,scope);
    }

    private void syncGoogleAccount(){
        if(isNetworkAvailable() ==  true){
            String[] accountarrs = getAccountNames();
            if (accountarrs.length > 0){
                getTask(LoginActivity.this,accountarrs[0], SCOPE).execute();
            }else{
                Toast.makeText(LoginActivity.this, "No google Account Sync!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "No Network Services!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = cm.getActiveNetworkInfo();
        if(networkinfo != null && networkinfo.isConnected()){
            Log.e("Network Testing", "Available");
            return true;
        }
        Log.e("Network Testing", "Not Available");
        return false;
    }
}