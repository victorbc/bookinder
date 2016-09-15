package equipe.projetoes.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
    private Class previous;
    private boolean isReadOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_livro);
        init();
        isReadOnly = getIntent().getBooleanExtra("readOnly", false);
        String livroNome = getIntent().getStringExtra("livroNome");
        dao = new LivroDAO(this);
        if(isReadOnly){
            livro = new Livro();
            livro.setNome(getIntent().getStringExtra("livroNome"));
            livro.setEditora(getIntent().getStringExtra("livroEditora"));
            livro.setPg(getIntent().getIntExtra("livroPg",0));
            livro.setImgFilePath(getIntent().getStringExtra("livroPath"));
            livro.setAutor(getIntent().getStringExtra("livroAutor"));
        }else
        livro = dao.getLivroByName(livroNome);

        try {
            previous = Class.forName("equipe.projetoes."
                    + getIntent().getStringExtra("previousActivity"));
        } catch (ClassNotFoundException e) {
            Log.v("ERRO_PREVIOUS_ACTIVITY", e.getMessage());
        }

        if (livro.getDrawable() != null)
            ((ImageView) findViewById(R.id.img)).setImageBitmap(livro.getDrawable());
        else
            ((ImageView) findViewById(R.id.img)).setImageResource(R.drawable.noimage);


        btFav = (ImageView) findViewById(R.id.btfav);
        btRead = (TextView) findViewById(R.id.txtpg);
        btTrade = (ImageView) findViewById(R.id.bttrade);

        if (isReadOnly) {
            btTrade.setVisibility(View.INVISIBLE);
            btFav.setVisibility(View.INVISIBLE);
            btRead.setVisibility(View.INVISIBLE);
            findViewById(R.id.textTrade).setVisibility(View.INVISIBLE);
            findViewById(R.id.textFav).setVisibility(View.INVISIBLE);
            findViewById(R.id.textPg).setVisibility(View.INVISIBLE);
        }

        if (!isReadOnly) {
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
        }

        nome = (TextView) findViewById(R.id.text_nome);
        autor = (TextView) findViewById(R.id.text_autor);
        editora = (TextView) findViewById(R.id.text_editora);
        paginas = (TextView) findViewById(R.id.text_pg);

        updateLivroInfo();
    }

    @Override
    public void onBackPressed() {
        if (previous != null && !previous.equals(""))
            startActivity(new Intent(this, previous));
        this.finish();
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
        String text = livro.getPg() + "";
        pg.setText(text);
        text = livro.getReadPg() + "";
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
        if (livro != null) {
            Log.v("CLICKED_BOOK", String.valueOf(livro.getNome() == null));
            Log.v("CLICKED_BOOK", String.valueOf(livro.getAutor() == null));
        }
        nome.setText(livro.getNome());
        autor.setText(livro.getAutor());
        editora.setText(livro.getEditora());
        if (livro.getPg() > 0)
            paginas.setText(livro.getPg() + "");
        else
            paginas.setText("Número indisponível de");
    }


}
