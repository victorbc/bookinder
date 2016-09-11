package equipe.projetoes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import equipe.projetoes.R;
import equipe.projetoes.utilis.AccDAO;
import equipe.projetoes.utilis.Global;

public class LoginFacebookActivity extends AppCompatActivity {
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {


            @Override
            public void onSuccess (LoginResult loginResult){
                GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            Log.i("json", me.toString());
                            if (response.getError() != null) {
                                // handle error
                            } else {
                                String email;
                                try {
                                    email = response.getJSONObject().get("email").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Global.currentAcc.setEmail_facebook(me.optString("email"));
                                AccDAO dao = new AccDAO(getApplicationContext());
                                if(Global.currentAcc.getId()!= null){
                                    dao.atualizaDadosDoAccount(Global.currentAcc);
                                }else{
                                    dao.adiciona(Global.currentAcc);
                                }
                                Log.i("token", AccessToken.getCurrentAccessToken().toString());
                                finish();
                                // send email and id to your web server
                            }
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email");
                    request.setParameters(parameters);
                    request.executeAsync();
            }


            @Override
            public void onCancel () {
                // info.setText("Login attempt canceled.");
                finish();
            }

            @Override
            public void onError (FacebookException e){
                Toast.makeText(LoginFacebookActivity.this, "Login attempt failed.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

}
