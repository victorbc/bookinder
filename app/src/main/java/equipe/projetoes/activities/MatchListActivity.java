package equipe.projetoes.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        this.init();

        if(!this.hasNavBar(getResources()))
            findViewById(R.id.navspace).setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_matches);

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
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(MatchListActivity.this, TrocaActivity.class);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public void updateMatches(){
        matches = new ArrayList<Match>();
        Random r = new Random();
        List<Livro> bibliotecaLocal = userDAO.listaTodos();
        List<Livro> tempList1 = new ArrayList<Livro>();
        List<Livro> tempList2  = new ArrayList<Livro>();


        for (int i=0; i<10; i++){
            int j = r.nextInt(10);
            for (j=0; j<11; j++){
                Collections.shuffle(bibliotecaLocal);
                tempList1 = bibliotecaLocal.subList(0, r.nextInt(bibliotecaLocal.size()));
                Collections.shuffle(bibliotecaLocal);
                tempList2 = bibliotecaLocal.subList(0, r.nextInt(bibliotecaLocal.size()));
//                while (tempList2.size() == 0){
//                    tempList2 = bibliotecaLocal.subList(0, r.nextInt(bibliotecaLocal.size()));
//                }
            }
                matches.add(new Match(tempList1, tempList2, "Usuario " + i+1, r.nextInt(1000)));
        }
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
