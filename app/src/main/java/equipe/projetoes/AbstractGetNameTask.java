package equipe.projetoes;

import android.content.Intent;
import android.media.MediaActionSound;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import equipe.projetoes.activities.HomeActivity;
import equipe.projetoes.activities.LoginActivity;
import equipe.projetoes.activities.MainActivity;

/**
 * Created by kallynnykarlla on 29/08/16.
 */
public abstract class AbstractGetNameTask extends AsyncTask<Void, Void, Void>{

    protected LoginActivity mActivity;
    public static String GOOGLE_USER_DATA = "no data";
    protected String mScope;
    protected String mEmail;
    protected int mRequest;

    public AbstractGetNameTask(LoginActivity mActivity, String mScope, String mEmail) {
        this.mActivity = mActivity;
        this.mScope = mScope;
        this.mEmail = mEmail;
    }

    @Override
    protected  Void doInBackground(Void... params) {
        try{
            fetchNameFromProfileServer();
        }catch (IOException ex){
            onError("Following Error ocurred, please try again. "
                    + ex.getMessage(),ex);

        }catch (JSONException e){
            onError("Bad response: "
                    + e.getMessage(),e);
        }
        return null;
    }

    protected void onError(String msg, Exception e){
        if( e != null){
            Log.e("", "Exception: ", e);
        }
    }

    protected abstract String fetchToken() throws IOException;

    private void fetchNameFromProfileServer()throws IOException, JSONException{
        String token = fetchToken();

        URL url = new URL("https://www.googleapis.com/oauth2" +
                "/v1/userinfo?access_token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int sc = con.getResponseCode();
        if (sc == 200){

            InputStream is = con.getInputStream();
            GOOGLE_USER_DATA = readResponse(is);
            is.close();

            Intent intent = new Intent(mActivity,HomeActivity.class);
            intent.putExtra("email_id", mEmail);
            mActivity.startActivity(intent);
            mActivity.finish();
            return;


        }else if ( sc == 401){
            GoogleAuthUtil.invalidateToken(mActivity,token);
            onError("Server auth error: " , null);


        }else {
            onError("Returned by server: "
                    + sc,null);
            return;
        }

    }

    private static String readResponse(InputStream is) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while((len = is.read(data, 0, data.length)) >= 0){
            bos.write(data,0,len);
        }
        return new String(bos.toByteArray(), "UTF-8");

    }

}
