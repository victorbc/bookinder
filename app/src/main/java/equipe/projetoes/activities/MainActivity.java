package equipe.projetoes.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import equipe.projetoes.R;
import equipe.projetoes.models.Livro;
import equipe.projetoes.utilis.HttpHandler;
import equipe.projetoes.utilis.LivroDAO;
import equipe.projetoes.utilis.OnSwipeTouchListener;

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
    private boolean isSlideLock = false;
    private Handler handler;
    private Animation animationFadeOut;
    private Animation animationFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.init();

        index = 0;

        http = new HttpHandler(this);
        dao = new LivroDAO(this);
        animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        handler = new Handler();

        //livros = http.getLivros();

        //livros = new ArrayList<Livro>();
        http.getLivros().add(new Livro(R.drawable.livro, "Game of Thrones", "George R.R", "Leya", 500));
        http.getLivros().add(new Livro(R.drawable.livro1, "Harry Potter e a pedra filosofal", "J.K. Rowling", "Racco", 372));
        http.getLivros().add(new Livro(R.drawable.livro2, "The Hunger Games", "Suzanne Collins", "Casa da Palavra", 429));
        http.getLivros().add(new Livro(R.drawable.livro3, "The Martian", "Matt Damon", "Escreva LTDA", 160));
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


//        if (http.isReady())
        setBooks();
//        else
//            new TimeOut().execute("1000");


        View.OnTouchListener swipeListner = new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                if (!isSlideLock) {
                    //Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                    if (!dao.listaTodos().contains(livro)) {
                        if (livro.getISBN() == null || livro.getISBN().equals("")) {
                            //TODO recuperar ISB da pesquisa
                            livro.setISBN(dao.listaTodos().size()+"");
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
                    shiftAnim();

                    anim.setImageResource(R.drawable.ic_unlike_type);
                    typeAnim();
                }
            }

            public void onSwipeBottom() {
                //Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
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
            livroView.setImageResource(nextLivro(1).getResId());
            livroView2.setImageResource(nextLivro(2).getResId());
            livroView3.setImageResource(nextLivro(3).getResId());
        } else {
            try {
                livroView.setImageBitmap(nextLivro(1).getDrawable());
                livroView2.setImageBitmap(nextLivro(2).getDrawable());
                livroView3.setImageBitmap(nextLivro(3).getDrawable());
            } catch (Exception e) {
                livroView.setImageResource(R.color.fade);
                livroView2.setImageResource(R.color.fade);
                livroView3.setImageResource(R.color.fade);
            }
        }
        updateLivroInfo();
    }

    private void updateLivroInfo() {
        nome.setText(livro.getNome());
        autor.setText(livro.getAutor());
        editora.setText(livro.getEditora());
        if(livro.getPg() > 0)
        paginas.setText(livro.getPg() + "");
        else
            paginas.setText("Número de páginas indisponível");
    }


    private void shiftBooks() {
        livroView.setImageDrawable(livroView2.getDrawable());
        livro = livro2;
        livroView2.setImageDrawable(livroView3.getDrawable());
        livro2 = livro3;
        try {
            if (nextLivro(3).getResId() != 0)
                livroView3.setImageResource(nextLivro(3).getResId());
            else
                livroView3.setImageBitmap(nextLivro(3).getDrawable());
        } catch (Exception e) {
            livroView3.setImageResource(R.color.fade);
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
                hotCountTxt.setText(Integer.toString(10));

            }
        });
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
            if (http.isReady())
                setBooks();
            else
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
}
