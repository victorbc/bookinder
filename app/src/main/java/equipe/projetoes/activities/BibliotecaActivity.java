package equipe.projetoes.activities;

import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import equipe.projetoes.R;
import equipe.projetoes.adapters.BibliotecaRecyclerAdapter;
import equipe.projetoes.models.Livro;

public class BibliotecaActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView mRecyclerView;
    private BibliotecaRecyclerAdapter adapter;
    private RecyclerView mRecyclerView2;
    private BibliotecaRecyclerAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);
        this.init();

        if (!this.hasNavBar(getResources()))
            findViewById(R.id.navspace).setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Livro> livros = new ArrayList<Livro>();
        livros.add(new Livro(R.drawable.livro, "Game of Thrones", "George R.R", "Leya", 500));
        livros.add(new Livro(R.drawable.livro1, "Game of Thrones", "George R.R", "Leya", 500));
        livros.add(new Livro(R.drawable.livro2, "Game of Thrones", "George R.R", "Leya", 500));
        livros.add(new Livro(R.drawable.livro3, "Game of Thrones", "George R.R", "Leya", 500));


        // specify an adapter (see also next example)
        adapter = new BibliotecaRecyclerAdapter(livros, BibliotecaRecyclerAdapter.NOINFO);
        mRecyclerView.setAdapter(adapter);


        mRecyclerView2 = (RecyclerView) findViewById(R.id.recycler2);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setNestedScrollingEnabled(false);

        // use a linear layout manager
        int rows = 1;
        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                rows = 1;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                rows = 1;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                rows = 2;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                rows = 2;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                rows = 3;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                rows = 3;
                break;
        }
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, rows);
        mGridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView2.setLayoutManager(mGridLayoutManager);

        ArrayList<Livro> livros2 = new ArrayList<Livro>();
        livros2.add(new Livro(R.drawable.livro, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro1, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro2, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro3, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro1, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro2, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro3, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro1, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro2, "Game of Thrones", "George R.R", "Leya", 500));
        livros2.add(new Livro(R.drawable.livro3, "Game of Thrones", "George R.R", "Leya", 500));


        // specify an adapter (see also next example)
        adapter2 = new BibliotecaRecyclerAdapter(livros2, BibliotecaRecyclerAdapter.FULL);
        mRecyclerView2.setAdapter(adapter2);

    }



}
