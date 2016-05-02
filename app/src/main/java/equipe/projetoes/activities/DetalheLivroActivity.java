package equipe.projetoes.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import equipe.projetoes.R;
import equipe.projetoes.models.Livro;
import equipe.projetoes.utilis.LivroDAO;

/**
 * Created by Victor on 4/26/2016.
 */
public class DetalheLivroActivity extends BaseActivity {
    private ImageView btFav;
    private TextView btRead;
    private ImageView btTrade;
    private Livro livro;
    private LivroDAO dao;
    private TextView nome;
    private TextView autor;
    private TextView editora;
    private TextView paginas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_livro);
        init();
        String livroNome = getIntent().getStringExtra("livroNome");
        dao = new LivroDAO(this);
        livro = dao.getLivroByName(livroNome);

        if (livroNome.equals("Game of Thrones")) {
            ((ImageView) findViewById(R.id.img)).setImageResource(R.drawable.livro);
        } else if (livroNome.equals("Game of Thrones 2")) {
            ((ImageView) findViewById(R.id.img)).setImageResource(R.drawable.livro1);

        } else if (livroNome.equals("Game of Thrones 3")) {
            ((ImageView) findViewById(R.id.img)).setImageResource(R.drawable.livro2);

        } else if (livroNome.equals("Game of Thrones 4")) {
            ((ImageView) findViewById(R.id.img)).setImageResource(R.drawable.livro3);

        }

        btFav = (ImageView) findViewById(R.id.btfav);
        btRead = (TextView) findViewById(R.id.txtpg);
        btTrade = (ImageView) findViewById(R.id.bttrade);

        btFav.setOnClickListener(infoAction);
        btRead.setOnClickListener(infoAction);
        btTrade.setOnClickListener(infoAction);


        if (livro.isFav()) {
            btFav.setColorFilter(ContextCompat.getColor(this, R.color.accent), PorterDuff.Mode.SRC_ATOP);
        }
        if (livro.isTradable()) {
            btTrade.setColorFilter(ContextCompat.getColor(this, R.color.accent), PorterDuff.Mode.SRC_ATOP);
        }
        btRead.setText(livro.getReadPg() + " / " + livro.getPg());


        nome = (TextView) findViewById(R.id.text_nome);
        autor = (TextView) findViewById(R.id.text_autor);
        editora = (TextView) findViewById(R.id.text_editora);
        paginas = (TextView) findViewById(R.id.text_pg);

        updateLivroInfo();
    }


    public View.OnClickListener infoAction = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btfav:
                    if (((ImageView) v).getColorFilter() == null) {
                        ((ImageView) v).setColorFilter(ContextCompat.getColor(v.getContext(), R.color.accent), PorterDuff.Mode.SRC_ATOP);
                        livro.setFav(true);

                    } else {
                        ((ImageView) v).setColorFilter(null);
                        livro.setFav(false);
                    }
                    break;
                case R.id.txtpg:
                    showPgDialog();
                    break;
                case R.id.bttrade:
                    if (((ImageView) v).getColorFilter() == null) {
                        ((ImageView) v).setColorFilter(ContextCompat.getColor(v.getContext(), R.color.accent), PorterDuff.Mode.SRC_ATOP);
                        livro.setTradable(true);
                    } else {
                        ((ImageView) v).setColorFilter(null);
                        livro.setTradable(false);
                    }
                    break;
            }

            dao.atualizaDadosDoLivro(livro);
        }
    };


    private void showPgDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pg, null);
        dialogBuilder.setView(dialogView);

        final EditText edpg = (EditText) dialogView.findViewById(R.id.edpg);
        TextView pg = (TextView) dialogView.findViewById(R.id.pg);
        String text = livro.getPg()+"";
        pg.setText(text);
        text = livro.getReadPg()+"";
        edpg.setText(text);



        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setTitle("Quantidade de páginas lidas");
        dialogView.findViewById(R.id.bt_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                livro.setReadPg(Integer.parseInt(edpg.getText().toString()));
                dao.atualizaDadosDoLivro(livro);
                btRead.setText(livro.getReadPg() + " / " + livro.getPg());
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }


    private void updateLivroInfo() {
        nome.setText(livro.getNome());
        autor.setText(livro.getAutor());
        editora.setText(livro.getEditora());
        paginas.setText(livro.getPg() + "");
    }


}
