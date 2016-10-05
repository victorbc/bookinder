package equipe.projetoes.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Random;

import equipe.projetoes.R;
import equipe.projetoes.data.RestDAO;
import equipe.projetoes.models.LivroUser;
import equipe.projetoes.util.Callback;
import equipe.projetoes.util.Constants;
import equipe.projetoes.util.gcm.RegistrationIntentService;
import equipe.projetoes.models.Livro;
import equipe.projetoes.util.Global;
import equipe.projetoes.util.HttpHandler;
import equipe.projetoes.data.LivroDAO;
import equipe.projetoes.util.OnSwipeTouchListener;

public class MainActivity extends BaseActivity {
    private ImageView livroView;
    private ImageView livroView2;
    private ImageView livroView3;
    private ImageView anim;
    private Livro livro;
    private Livro livro2;
    private Livro livro3;
    private TextView nome;
    private TextView autor;
    private TextView editora;
    private TextView paginas;
    // private List<Livro> livros;
    private Random rnd;
    private TextView hotCountTxt;
    private HttpHandler http;
    private int index;
    private LivroDAO dao;
    private RestDAO restDAO;
    private boolean isSlideLock = false;
    private Handler handler;
    private Animation animationFadeOut;
    private Animation animationFadeIn;
    private View progressBar;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        restDAO = new RestDAO(Constants.DEFAULT_HOST);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(SENT_TOKEN_TO_SERVER, false);

            }
        };

        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        this.init();

        index = 0;

        http = new HttpHandler(this);
        dao = new LivroDAO(this);
        animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        handler = new Handler();

        //livros = http.getLivros();

        //livros = new ArrayList<Livro>();
        rnd = new Random();


        livroView = (ImageView) findViewById(R.id.livro);
        livroView2 = (ImageView) findViewById(R.id.livro2);
        livroView3 = (ImageView) findViewById(R.id.livro3);

        nome = (TextView) findViewById(R.id.text_nome);
        autor = (TextView) findViewById(R.id.text_autor);
        editora = (TextView) findViewById(R.id.text_editora);
        paginas = (TextView) findViewById(R.id.text_pg);

        anim = (ImageView) findViewById(R.id.anim);


        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        //findViewById(R.id.info).setVisibility(View.INVISIBLE);
        findViewById(R.id.livros).setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.progressBar);


        if (http.isReady()) {
            progressBar.setVisibility(View.INVISIBLE);
            setBooks();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            new TimeOut().execute("1000");
        }


        View.OnTouchListener swipeListner = new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                if (!isSlideLock) {
                    //Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                    if (!dao.listaTodos().contains(livro)) {
                        if (livro.getISBN() == null || livro.getISBN().equals("")) {
                            //TODO recuperar ISB da pesquisa
                            livro.setISBN(dao.listaTodos().size() + "");
                        }
                        dao.adiciona(livro);

                        anim.setImageResource(R.drawable.ic_book_type);
                        typeAnim();
                    }
                    shiftAnim();
                }
            }

            public void onSwipeRight() {
                if (!isSlideLock) {
                    shiftAnim();

                    anim.setImageResource(R.drawable.ic_trade_type);
                    typeAnim();
                }
            }

            public void onSwipeLeft() {
                if (!isSlideLock) {
                    restDAO.getLivro(livro.getISBN(), new Callback<Livro>() {
                        @Override
                        public void execute(Livro result) {
                            if(result == null){
                                restDAO.addBookToLibrary(livro, new Callback<LivroUser>() {
                                    @Override
                                    public void execute(LivroUser result) {
                                        if(result != null){
                                           // restDAO.update(result.b);
                                        }
                                    }
                                });
                            }
                        }
                    });
                    shiftAnim();
                    anim.setImageResource(R.drawable.ic_unlike_type);
                    typeAnim();
                }
            }

            public void onSwipeBottom() {
                if (!isSlideLock) {
                    anim.setImageResource(R.drawable.ic_info_type);
                    typeAnim();
                    handler.postDelayed(detailDelay, 1000);

                }
            }

        };


        livroView.setOnTouchListener(swipeListner);
        livroView2.setOnTouchListener(swipeListner);
        livroView3.setOnTouchListener(swipeListner);
    }


    private void shiftAnim() {
        livroView.startAnimation(animationFadeOut);
        isSlideLock = true;
        handler.postDelayed(shiftDelay, 1000);
    }

    private void typeAnim() {
        anim.setVisibility(View.VISIBLE);
        anim.startAnimation(animationFadeIn);
        handler.postDelayed(animDelay, 800);
    }

    private void setBooks() {
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        //findViewById(R.id.info).setVisibility(View.VISIBLE);
        findViewById(R.id.livros).setVisibility(View.VISIBLE);
        if (!http.isReady()) {
        } else {
            try {
                if (nextLivro(1).getDrawable() != null)
                    livroView.setImageBitmap(nextLivro(1).getDrawable());
                else
                    livroView.setImageResource(R.drawable.noimage);
                if (nextLivro(2).getDrawable() != null)
                    livroView2.setImageBitmap(nextLivro(2).getDrawable());
                else
                    livroView2.setImageResource(R.drawable.noimage);
                if (nextLivro(3).getDrawable() != null)
                    livroView3.setImageBitmap(nextLivro(3).getDrawable());
                else
                    livroView3.setImageResource(R.drawable.noimage);
            } catch (Exception e) {
                livroView.setImageResource(R.drawable.noimage);
                livroView2.setImageResource(R.drawable.noimage);
                livroView3.setImageResource(R.drawable.noimage);
            }
        }
        updateLivroInfo();
    }

    private void updateLivroInfo() {
        nome.setText(livro.getNome());
        autor.setText(livro.getAutor());
        editora.setText(livro.getEditora());
        if (livro.getPg() > 0)
            paginas.setText(livro.getPg() + "");
        else
            paginas.setText("Número indisponível de");
    }


    private void shiftBooks() {
        if (livroView2.getDrawable() != null)
            livroView.setImageDrawable(livroView2.getDrawable());
        else
            livroView.setImageResource(R.drawable.noimage);
        livro = livro2;
        if (livroView3.getDrawable() != null)
            livroView2.setImageDrawable(livroView3.getDrawable());
        else
            livroView2.setImageResource(R.drawable.noimage);

        livro2 = livro3;
        try {
            if (nextLivro(3).getResId() != 0)
                livroView3.setImageResource(nextLivro(3).getResId());
            else {
                if (nextLivro(3).getDrawable() != null)
                    livroView3.setImageBitmap(nextLivro(3).getDrawable());
                else
                    livroView3.setImageResource(R.drawable.noimage);
            }
        } catch (Exception e) {
            livroView3.setImageResource(R.drawable.noimage);
        }
        updateLivroInfo();
    }

    private Livro nextLivro(int pos) {
        Livro temp = http.getLivros().get(nextPos());
        //System.out.println(""+http.getLivros().size());
        switch (pos) {
            case 1:
                livro = temp;
                break;
            case 2:
                livro2 = temp;
                break;
            case 3:
                livro3 = temp;
                break;
        }
        return temp;
    }

    private int nextPos() {
        if (index >= http.getLivros().size())
            index = 0;
        else {
            index++;
            if (index % 5 == 0) {
                http.getBooks(10);
                http.getCovers(index, 10);
            }
        }
        return index;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        //notifCountTxt = (TextView) notifCount.findViewById(R.id.notif_count);
        hotCountTxt = ((TextView) notifCount.findViewById(R.id.notif_count));
        hotCountTxt.setText("0");
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MatchListActivity.class));
            }
        });
        updateHotCount();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_location) {
            showLocationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLocationDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_localizacao, null);
        dialogBuilder.setView(dialogView);


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setTitle("Mostrar livros por localização");
        dialogView.findViewById(R.id.bt_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }


    public final void updateHotCount() {
        if (hotCountTxt == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                hotCountTxt.setVisibility(View.VISIBLE);
                //hotCountTxt.setText(Integer.toString(GlobalAccess.NOTIFICATION_COUNT));

                hotCountTxt.setText(Integer.toString(Global.numMatches));

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getIntent() != null) {
            try {
                Class c = Class.forName("equipe.projetoes."
                        + getIntent().getStringExtra("previousActivity"));
                startActivity(new Intent(this, c));
            } catch (ClassNotFoundException e) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
        finish();
    }

    private class TimeOut extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... time) {
            int t = Integer.parseInt(time[0]);
            while (t > 0) t--;

            return "";
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (http.isReady()) {
                setBooks();
                progressBar.setVisibility(View.INVISIBLE);
            } else
                new TimeOut().execute("1000");

        }
    }

    private Runnable shiftDelay = new Runnable() {
        public void run() {
            shiftBooks();
            isSlideLock = false;
        }
    };

    private Runnable animDelay = new Runnable() {
        public void run() {
            anim.setVisibility(View.INVISIBLE);
        }
    };

    private Runnable detailDelay = new Runnable() {
        public void run() {

            Intent intent = new Intent(getBaseContext(), DetalheLivroActivity.class);
            intent.putExtra("livroNome", livro.getNome());
            intent.putExtra("livroEditora", livro.getEditora());
            intent.putExtra("livroAutor", livro.getAutor());
            intent.putExtra("livroPg", livro.getPg());
            intent.putExtra("livroPath", livro.getImgFilePath());
            intent.putExtra("readOnly", true);
            startActivity(intent);
            isSlideLock = false;
        }
    };

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

}
