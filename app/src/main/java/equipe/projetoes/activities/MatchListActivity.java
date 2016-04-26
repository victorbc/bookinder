package equipe.projetoes.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import equipe.projetoes.R;
import equipe.projetoes.adapters.MatchesRecyclerAdapter;
import equipe.projetoes.models.Match;

public class MatchListActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int selectedMenuId;
    private Menu menu;
    private Drawable tempDrawable;
    private RecyclerView mRecyclerView;
    private MatchesRecyclerAdapter adapter;

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

        ArrayList<Match> matches = new ArrayList<Match>();
        matches.add(new Match());
        matches.add(new Match());
        matches.add(new Match());
        matches.add(new Match());
        matches.add(new Match());
        matches.add(new Match());
        matches.add(new Match());
        matches.add(new Match());
        matches.add(new Match());
        matches.add(new Match());

        // specify an adapter (see also next example)
        adapter = new MatchesRecyclerAdapter(matches);
        mRecyclerView.setAdapter(adapter);
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
