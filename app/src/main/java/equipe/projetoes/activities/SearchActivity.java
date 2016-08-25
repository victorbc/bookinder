package equipe.projetoes.activities;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import equipe.projetoes.models.Filtros;
import equipe.projetoes.R;
import equipe.projetoes.adapters.SearchRecyclerAdapter;
import equipe.projetoes.models.Livro;
import equipe.projetoes.utilis.HttpHandler;
import equipe.projetoes.utilis.LivroDAO;

/**
 * Created by Victor Batista on 5/5/2016.
 */
public class SearchActivity extends BaseActivity {

    private LivroDAO dao;
    private List<Livro> list;
    private SearchView searchView;
    private RecyclerView mRecyclerView;
    private SearchRecyclerAdapter adapter;
    private LinearLayout list_filters;
    private String search_input;
    private Filtros selected_filter = Filtros.TITULO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();

        dao = new LivroDAO(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        list_filters = (LinearLayout) findViewById(R.id.list_filters);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        adapter = new SearchRecyclerAdapter(new ArrayList<Livro>(), this);
        mRecyclerView.setAdapter(adapter);

        list_filters.setVisibility(View.INVISIBLE);



//        addFilterButton("Titulo", R.drawable.ic_edit );
//        addFilterButton("ISBN", R.drawable.ic_add );
//        addFilterButton("Autor", R.drawable.ic_add );
//        addFilterButton("Editora", R.drawable.ic_add );
//        addFilterButton("Ano", R.drawable.ic_add );


    }

    public void buttonOnClick(View view) {
        findViewById(R.id.b_titulo).setAlpha(0.4f);
        findViewById(R.id.b_isbn).setAlpha(0.4f);
        findViewById(R.id.b_autor).setAlpha(0.4f);
        findViewById(R.id.b_editora).setAlpha(0.4f);
        findViewById(R.id.b_ano).setAlpha(0.4f);

        switch(view.getId())
        {
            case R.id.b_titulo:
                selected_filter = Filtros.TITULO;
                findViewById(R.id.b_titulo).setAlpha(1f);
                break;
            case R.id.b_isbn:
                selected_filter = Filtros.ISBN;
                findViewById(R.id.b_isbn).setAlpha(1f);
                break;
            case R.id.b_autor:
                selected_filter = Filtros.AUTOR;
                findViewById(R.id.b_autor).setAlpha(1f);
                break;
            case R.id.b_editora:
                selected_filter = Filtros.EDITORA;
                findViewById(R.id.b_editora).setAlpha(1f);
                break;
            case R.id.b_ano:
                selected_filter = Filtros.ANO;
                findViewById(R.id.b_ano).setAlpha(1f);
                break;
        }
        handleQuery(search_input);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        list_filters.setVisibility(View.INVISIBLE);

    }


    private void addFilterButton(String name, int icon){
        Button b = new Button(mRecyclerView.getContext());
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setWidth(50);
        b.setText("  " + name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            b.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        Drawable img = mRecyclerView.getContext().getResources().getDrawable( icon );
        img.setBounds( 0, 0, 50, 50 );
        b.setCompoundDrawables( img, null, null, null );
        list_filters.addView(b.getRootView());
    }




    @Override
    protected void onResume() {
        super.onResume();
        list = dao.listaTodos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            list_filters.setVisibility(View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);




        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String string) {
                search_input = string;
                handleQuery(string);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String string) {
                search_input = string;
                handleQuery(string);
                return true;
            }
        });

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                list_filters.setVisibility(View.INVISIBLE);
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });

        return true;
    }


    private void handleQuery(String string) {
        String query = removerAcentos(string);
        ArrayList<Livro> result = new ArrayList<Livro>();
        String label;
        if (!string.equals("")) {
            for (Livro item : list) {
                switch (selected_filter){
                    case TITULO:
                        label = removerAcentos(item.getNome().toLowerCase());
                        break;
                    case ISBN:
                        label = removerAcentos(item.getISBN().toLowerCase());
                        break;
                    case AUTOR:
                        label = removerAcentos(item.getAutor().toLowerCase());
                        break;
                    case EDITORA:
                        label = removerAcentos(item.getEditora().toLowerCase());
                        break;
                    case ANO:
                        label = removerAcentos(item.getNome().toLowerCase());
                        break;
                    default:
                        label = removerAcentos(item.getNome().toLowerCase());
                        break;
                }
                if (label.contains(query.toLowerCase())) {
                    result.add(item);
                }
            }
        }

        updateList(result, string);
    }

    public void updateList(List<Livro> result, String string) {
        //((TextView) findViewById(R.id.sample_output)).setText("");
        List<Livro> onlyRemove = new ArrayList<Livro>(adapter.getmDataset());
        List<Livro> onlyAdd = new ArrayList<Livro>(result);

        for (Livro item : onlyRemove) {
            if (onlyAdd.contains(item)) onlyAdd.remove(item);
            else
                adapter.remove(item);
        }
        for (Livro item : onlyAdd) {
            adapter.add(adapter.getItemCount(), item);
        }

    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }


}
