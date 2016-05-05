package equipe.projetoes.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import equipe.projetoes.R;
import equipe.projetoes.adapters.SearchRecyclerAdapter;
import equipe.projetoes.models.Livro;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();

        dao = new LivroDAO(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);


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

    }

    @Override
    protected void onResume() {
        super.onResume();
        list = dao.listaTodos();
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
                handleQuery(string);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String string) {

                handleQuery(string);

                return true;
            }
        });


        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }


    private void handleQuery(String string) {
        String query = removerAcentos(string);
        ArrayList<Livro> result = new ArrayList<Livro>();
        if (!string.equals("")) {
            for (Livro item : list) {
                String label = removerAcentos(item.getNome().toLowerCase());
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
