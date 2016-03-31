package equipe.projetoes.activities;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import equipe.projetoes.R;
import equipe.projetoes.models.Livro;
import equipe.projetoes.utilis.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        livros = new ArrayList<Livro>();
        livros.add(new Livro(R.drawable.livro, "Game of Thrones", "George R.R", "Leya", 500));
        livros.add(new Livro(R.drawable.livro1, "Harry Potter e a pedra filosofal", "J.K. Rowling", "Racco", 372));
        livros.add(new Livro(R.drawable.livro2, "The Hunger Games", "Suzanne Collins", "Casa da Palavra", 429));
        livros.add(new Livro(R.drawable.livro3, "The Martian", "Matt Damon", "Escreva LTDA", 160));
        rnd = new Random();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setTitle("");
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
        toolbar.setPadding(0, (int) px, 0, 0);
        //toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setElevation(0);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.setScrimColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
        paginas.setText(livro.getPg()+"");
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        //notifCountTxt = (TextView) notifCount.findViewById(R.id.notif_count);
        ((TextView) notifCount.findViewById(R.id.notif_count)).setText("0");
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            return true;
        }
        if (id == R.id.action_notifications) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_trocas) {
            // Handle the camera action
        } else if (id == R.id.nav_aguardando) {

        } else if (id == R.id.nav_buscar) {

        } else if (id == R.id.nav_biblioteca) {

        } else if (id == R.id.nav_pref) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public final void updateHotCount() {
//        if (notifCountTxt == null) { return; }
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (GlobalAccess.NOTIFICATION_COUNT == 0) {
//                    notifCountTxt.setVisibility(View.INVISIBLE);
//                }else {
//                    notifCountTxt.setVisibility(View.VISIBLE);
//                    notifCountTxt.setText(Integer.toString(GlobalAccess.NOTIFICATION_COUNT));
//                }
//            }
//        });
    }

}
