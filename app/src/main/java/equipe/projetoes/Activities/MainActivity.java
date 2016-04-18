package equipe.projetoes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import equipe.projetoes.R;
import equipe.projetoes.models.Livro;
import equipe.projetoes.utilis.OnSwipeTouchListener;

public class MainActivity extends BaseActivity {
    private ImageView livroView;
    private ImageView livroView2;
    private ImageView livroView3;
    private Livro livro;
    private Livro livro2;
    private Livro livro3;
    private TextView nome;
    private TextView autor;
    private TextView editora;
    private TextView paginas;
    private List<Livro> livros;
    private Random rnd;
    private TextView hotCountTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.init();

        livros = new ArrayList<Livro>();
        livros.add(new Livro(R.drawable.livro, "Game of Thrones", "George R.R", "Leya", 500));
        livros.add(new Livro(R.drawable.livro1, "Harry Potter e a pedra filosofal", "J.K. Rowling", "Racco", 372));
        livros.add(new Livro(R.drawable.livro2, "The Hunger Games", "Suzanne Collins", "Casa da Palavra", 429));
        livros.add(new Livro(R.drawable.livro3, "The Martian", "Matt Damon", "Escreva LTDA", 160));
        rnd = new Random();


        livroView = (ImageView) findViewById(R.id.livro);
        livroView2 = (ImageView) findViewById(R.id.livro2);
        livroView3 = (ImageView) findViewById(R.id.livro3);

        nome = (TextView) findViewById(R.id.text_nome);
        autor = (TextView) findViewById(R.id.text_autor);
        editora = (TextView) findViewById(R.id.text_editora);
        paginas = (TextView) findViewById(R.id.text_pg);

        livroView.setImageResource(nextLivro(1).getResId());
        livroView2.setImageResource(nextLivro(2).getResId());
        livroView3.setImageResource(nextLivro(3).getResId());

        updateLivroInfo();


        View.OnTouchListener swipeListner = new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                //Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                shiftBooks();
            }

            public void onSwipeRight() {
                // Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                shiftBooks();
            }

            public void onSwipeLeft() {
                // Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                shiftBooks();
            }

            public void onSwipeBottom() {
                //Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        };


        livroView.setOnTouchListener(swipeListner);
        livroView2.setOnTouchListener(swipeListner);
        livroView3.setOnTouchListener(swipeListner);


    }

    private void updateLivroInfo() {
        nome.setText(livro.getNome());
        autor.setText(livro.getAutor());
        editora.setText(livro.getEditora());
        paginas.setText(livro.getPg() + "");
    }


    private void shiftBooks() {
        livroView.setImageDrawable(livroView2.getDrawable());
        livro = livro2;
        livroView2.setImageDrawable(livroView3.getDrawable());
        livro2 = livro3;
        livroView3.setImageResource(nextLivro(3).getResId());

        updateLivroInfo();
    }

    private Livro nextLivro(int pos) {
        Livro temp = livros.get(rnd.nextInt(livros.size()));
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
                hotCountTxt.setText(Integer.toString(10));

            }
        });
    }

}
