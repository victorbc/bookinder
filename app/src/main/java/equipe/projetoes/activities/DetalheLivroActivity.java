package equipe.projetoes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import equipe.projetoes.R;
import equipe.projetoes.models.Livro;
import equipe.projetoes.utilis.LivroDAO;

/**
 * Created by Victor on 4/26/2016.
 */
public class DetalheLivroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_livro);
        init();
        String livroNome = getIntent().getStringExtra("livroNome");
        LivroDAO dao = new LivroDAO(this);
        Livro livro = dao.getLivroByName(livroNome);

        if (livroNome.equals("Game of Thrones")) {
            ((ImageView) findViewById(R.id.img)).setImageResource(R.drawable.livro);
        } else if (livroNome.equals("Game of Thrones 2")) {
            ((ImageView) findViewById(R.id.img)).setImageResource(R.drawable.livro1);

        } else if (livroNome.equals("Game of Thrones 3")) {
            ((ImageView) findViewById(R.id.img)).setImageResource(R.drawable.livro2);

        } else if (livroNome.equals("Game of Thrones 4")) {
            ((ImageView) findViewById(R.id.img)).setImageResource(R.drawable.livro3);

        }

    }

}
