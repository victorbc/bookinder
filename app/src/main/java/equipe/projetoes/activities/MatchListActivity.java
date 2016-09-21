package equipe.projetoes.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.github.siyamed.shapeimageview.CircularImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import equipe.projetoes.R;
import equipe.projetoes.adapters.MatchesRecyclerAdapter;
import equipe.projetoes.models.Account;
import equipe.projetoes.models.Livro;
import equipe.projetoes.models.Match;
import equipe.projetoes.utilis.AccDAO;
import equipe.projetoes.utilis.Global;
import equipe.projetoes.utilis.LivroDAO;
import equipe.projetoes.utilis.RecyclerItemClickListener;

public class MatchListActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int selectedMenuId;
    private Menu menu;
    private Drawable tempDrawable;
    private RecyclerView mRecyclerView;
    private MatchesRecyclerAdapter adapter;
    private LivroDAO userDAO;
    private AccDAO accDAO;
    ArrayList<Match> matches;


    private ImageView image1;
    private int[] imageArray;
    private int currentIndex;
    private int startIndex;
    private int endIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        this.init();

        if(!this.hasNavBar(getResources()))
            findViewById(R.id.navspace).setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_matches);

        image1 = (ImageView) findViewById(R.id.match_pic1);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        userDAO = new LivroDAO(this);
        accDAO = new AccDAO(this);
        updateMatches();


        // specify an adapter (see also next example)
        adapter = new MatchesRecyclerAdapter(matches);
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                Intent intent = new Intent(MatchListActivity.this, MatchInfoActivity.class);
//                mRecyclerView.getAdapter().g
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
    }

    public void updateMatches(){
        matches = new ArrayList<Match>();
        Random r = new Random();
        long seed;
        List<Livro> bibliotecaLocal = new ArrayList<Livro>();
        bibliotecaLocal.addAll(userDAO.listaTodos());
        List<Livro> tempList1;
        List<Livro> tempList2;

        if (bibliotecaLocal.size() >= 2){
            for (int i=0; i<10; i++){ //Número de matches
                tempList1 = new ArrayList<Livro>();
                tempList2 = new ArrayList<Livro>();

                seed = System.nanoTime();
                Collections.shuffle(bibliotecaLocal, new Random(seed));
                tempList1.addAll(bibliotecaLocal.subList(0, r.nextInt(bibliotecaLocal.size())));
                while (tempList1.size() < 1){
                    tempList1.addAll(bibliotecaLocal.subList(0, r.nextInt(bibliotecaLocal.size())));
                }
                Log.i("livros temp 1", tempList1.toString());

                seed = System.nanoTime();
                Collections.shuffle(bibliotecaLocal, new Random(seed));
                tempList2.addAll(bibliotecaLocal.subList(0, r.nextInt(bibliotecaLocal.size())));
                while (tempList2.size() < 1){
                    tempList2.addAll(bibliotecaLocal.subList(0, r.nextInt(bibliotecaLocal.size())));
                }
                Log.i("livros temp 2", tempList2.toString());

                matches.add(new Match(tempList1, tempList2, "Usuario_" + (i+1), r.nextInt(1000), createTumb(tempList1), createTumb(tempList2)));
            }
        }
    }

    private AnimationDrawable createTumb(List<Livro> tempList) {
        Drawable d;
        AnimationDrawable animation = new AnimationDrawable();


        Resources r = getResources();
        for (Livro livro: tempList){
            d = new BitmapDrawable(getResources(), livro.getDrawable());
            animation.addFrame(d, 2000);
        }

        Log.i("animation obj", animation.toString());
//        animation.addFrame(r.getDrawable(R.drawable.livro1), 2500);
//        animation.addFrame(r.getDrawable(R.drawable.livro2), 2500);
//        animation.addFrame(r.getDrawable(R.drawable.livro3), 2500);

//        Drawable[] layers = new Drawable[3];
//        layers[0] = r.getDrawable(R.drawable.livro);
//        layers[1] = r.getDrawable(R.drawable.livro1);
//        layers[2] = r.getDrawable(R.drawable.livro3);

//        TransitionDrawable transition = new TransitionDrawable(layers);
//        transition.startTransition(1500);
//        return transition;

        return animation;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matched, menu);
        MenuItem item = menu.findItem(R.id.action_sort_location);
        tempDrawable = item.getIcon();
        if (tempDrawable != null) {
            tempDrawable.mutate();
            tempDrawable.setColorFilter(ContextCompat.getColor(MatchListActivity.this, R.color.accent), PorterDuff.Mode.SRC_ATOP);
        }
        selectedMenuId = 2;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_sort_qt) {
            if (selectedMenuId == 0) {
                item.setIcon(R.drawable.ic_order_desc_qt);
                selectedMenuId = 1;
            } else if (selectedMenuId == 1) {
                item.setIcon(R.drawable.ic_order_asc_qt);
                selectedMenuId = 0;
            } else if (selectedMenuId > 1) selectedMenuId = 0;

            tempDrawable = item.getIcon();
            if (tempDrawable != null) {
                tempDrawable.mutate();
                tempDrawable.setColorFilter(ContextCompat.getColor(MatchListActivity.this, R.color.accent), PorterDuff.Mode.SRC_ATOP);
            }


            tempDrawable = menu.findItem(R.id.action_sort_location).getIcon();
            if (tempDrawable != null) {
                tempDrawable.mutate();
                tempDrawable.setColorFilter(null);
            }

            return true;
        }
        if (id == R.id.action_sort_location) {

            if (selectedMenuId == 2) {
                item.setIcon(R.drawable.ic_order_desc_loc);
                selectedMenuId = 3;
            } else if (selectedMenuId == 3) {
                item.setIcon(R.drawable.ic_order_asc_loc);
                selectedMenuId = 2;
            }else if (selectedMenuId < 2) selectedMenuId = 2;

            tempDrawable = item.getIcon();
            if (tempDrawable != null) {
                tempDrawable.mutate();
                tempDrawable.setColorFilter(ContextCompat.getColor(MatchListActivity.this, R.color.accent), PorterDuff.Mode.SRC_ATOP);
            }


            tempDrawable = menu.findItem(R.id.action_sort_qt).getIcon();
            if (tempDrawable != null) {
                tempDrawable.mutate();
                tempDrawable.setColorFilter(null);
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
