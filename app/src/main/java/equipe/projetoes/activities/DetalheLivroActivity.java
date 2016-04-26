package equipe.projetoes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import equipe.projetoes.R;

/**
 * Created by Victor on 4/26/2016.
 */
public class DetalheLivroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_livro);
        init();
    }

}
