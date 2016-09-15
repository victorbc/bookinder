package equipe.projetoes.activities;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import equipe.projetoes.R;
import equipe.projetoes.models.Account;
import equipe.projetoes.utilis.AccDAO;
import equipe.projetoes.utilis.Global;

public class PreferenciasActivity extends BaseActivity {

    private Switch conta_facebook;
    private Switch conta_google;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        if (Global.currentAcc == null){
            Global.currentAcc = new Account();
        }
        conta_facebook = (Switch) findViewById(R.id.switch_facebook);
        conta_google = (Switch) findViewById(R.id.switch_google);

        if(Global.currentAcc.getEmail_google() != null && !Global.currentAcc.getEmail_google().isEmpty()){
            conta_google.setChecked(true);
        }
        if(Global.currentAcc.getEmail_facebook() != null && !Global.currentAcc.getEmail_facebook().isEmpty()){
            conta_facebook.setChecked(true);
        }

        conta_google.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // associar conta do google
                    Intent intent = new Intent(PreferenciasActivity.this, LoginGoogleActivity.class);
                    startActivity(intent);

                    Log.d("Preferencia", "Deu certo colocar");
                }else{
                    // desassociar conta do google.
                    Global.currentAcc.setEmail_google(null);
                    AccDAO dao = new AccDAO(getApplicationContext());
                    dao.atualizaDadosDoAccount(Global.currentAcc);
                    Log.d("Preferencia", "Deu certo tirar");

                }
            }
        });

        conta_facebook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // associar conta do facebook

                    Intent intent = new Intent(PreferenciasActivity.this, LoginFacebookActivity.class);
                    startActivity(intent);
                    Log.d("Preferencia", "Deu certo colocar");
                }else{
                    // desassociar conta do facebook.

                    Global.currentAcc.setEmail_facebook(null);
                    AccDAO dao = new AccDAO(getApplicationContext());
                    dao.atualizaDadosDoAccount(Global.currentAcc);
                    Log.d("Preferencia", "Deu certo tirar");
                }
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
}
