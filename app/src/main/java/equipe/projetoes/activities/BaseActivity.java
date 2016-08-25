package equipe.projetoes.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


import equipe.projetoes.R;

/**
 * Created by Victor on 4/9/2016.
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


    }

    protected void init() {
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

        if (this instanceof MainActivity)
            navigationView.setCheckedItem(R.id.nav_trocas);
        if (this instanceof BibliotecaActivity)
            navigationView.setCheckedItem(R.id.nav_biblioteca);
        if (this instanceof MatchListActivity)
            navigationView.setCheckedItem(R.id.nav_aguardando);
        if (this instanceof SearchActivity)
            navigationView.setCheckedItem(R.id.nav_buscar);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_trocas) {
            if (!(this instanceof MainActivity)) {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        } else if (id == R.id.nav_aguardando) {
            if (!(this instanceof MatchListActivity)) {
                startActivity(new Intent(this, MatchListActivity.class));
            }
            finish();
        } else if (id == R.id.nav_buscar) {
            if (!(this instanceof SearchActivity)) {
                startActivity(new Intent(this, SearchActivity.class));
            }
        } else if (id == R.id.nav_biblioteca) {
            if (!(this instanceof BibliotecaActivity)) {
                startActivity(new Intent(this, BibliotecaActivity.class));
            }
            finish();
        } else if (id == R.id.nav_pref) {

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            if (!(this instanceof MainActivity)){
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }

    public boolean hasNavBar (Resources resources)
    {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }
}
