package equipe.projetoes.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import equipe.projetoes.AbstractGetNameTask;
import equipe.projetoes.R;

public class HomeActivity extends AppCompatActivity {

    ImageView imageProfile;
    TextView textViewnName, textViewEmail;
    String textName, textEmail, userImageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_login);

        textViewnName = (TextView) findViewById(R.id.textViewNameValue);
        textViewEmail = (TextView) findViewById(R.id.textViewEmailValue);
        imageProfile = (ImageView) findViewById(R.id.ImageView21);

        Intent intent = getIntent();
        textEmail = intent.getStringExtra("email_id");
        System.out.println(textEmail);
        textViewEmail.setText(textEmail);

        try {
            JSONObject profileData = new JSONObject(AbstractGetNameTask.GOOGLE_USER_DATA);

            if (profileData.has("picture")) {
                userImageUrl = profileData.getString("picture");
                new GetImageFromUrl().execute(userImageUrl);
            }
            if (profileData.has("name")) {
                textName = profileData.getString("name");
                textViewnName.setText("textName");
            }
            if (profileData.has("name")) {
                textName = profileData.getString("name");
                textViewnName.setText("textName");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

        private class GetImageFromUrl extends AsyncTask<String, Void, Bitmap>{


            @Override
            protected Bitmap doInBackground(String... urls){
                Bitmap map = null;
                for(String url: urls){
                    map = downloadImage(url);
                }
                return map;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap){
                imageProfile.setImageBitmap(bitmap);
            }

            private Bitmap downloadImage(String url){
                Bitmap bitmap = null;
                InputStream stream = null;
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1;

                try{
                    stream = getHttpConnection(url);
                    bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                    stream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
                return bitmap;
            }

            private InputStream getHttpConnection(String urlString) throws IOException{
                InputStream stream = null;
                URL url = new URL(urlString);
                URLConnection connection = url.openConnection();

                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    if( httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        stream = httpURLConnection.getInputStream();
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return stream;
            }
        }
    }


