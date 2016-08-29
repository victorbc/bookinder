package equipe.projetoes.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.widget.ListView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

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

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final int RC_BARCODE_TYPED = 9002;
    private static final String TAG = "BarcodeMain";
    private static final int LER_CODIGO_DE_BARRA = 9000;
    private LivroDAO dao;
    private List<Livro> list;
    private SearchView searchView;
    private RecyclerView mRecyclerView;
    private SearchRecyclerAdapter adapter;
    private LinearLayout list_filters;
    private String search_input;
    private Filtros selected_filter;
    private MenuItem menuSearch;
    private HttpHandler httpHandler;
    private View progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();

        httpHandler = new HttpHandler(this);
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

        selected_filter = Filtros.TITULO;
        search_input = "";
        list_filters.setVisibility(View.INVISIBLE);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

    }

    public void buttonOnClick(View view) {
        turnFiltersGray();

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
            default:
                selected_filter = Filtros.TITULO;
                findViewById(R.id.b_titulo).setAlpha(1f);
                break;
        }
        handleQuery(search_input);
    }

    private void turnFiltersGray() {
        findViewById(R.id.b_titulo).setAlpha(0.4f);
        findViewById(R.id.b_isbn).setAlpha(0.4f);
        findViewById(R.id.b_autor).setAlpha(0.4f);
        findViewById(R.id.b_editora).setAlpha(0.4f);
        findViewById(R.id.b_ano).setAlpha(0.4f);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        list_filters.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        list = dao.listaTodos();  //Pegar livros do BD local
        list = httpHandler.getLivros();  //Pegar livros da internet

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
        }if(id == R.id.code_reader){
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        menuSearch = menu.findItem(R.id.search);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String string) {
                search_input = string;
                httpHandler.getBooks(20, selected_filter, string);
                new TimeOut().execute("1000");
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String string) {
                if (!string.equals("")){
                    progressBar.setVisibility(View.VISIBLE);
                }
                search_input = string;
                httpHandler.getBooks(20, selected_filter, string);
                new TimeOut().execute("1000");
                return true;
            }
        });

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                list_filters.setVisibility(View.INVISIBLE);
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });

        return true;
    }


    public void handleQuery(String string) {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to

        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    list_filters.setVisibility(View.VISIBLE);
                    menuSearch.expandActionView();
                    searchView.setQuery(barcode.displayValue, false);

                    selected_filter=Filtros.ISBN;
                    turnFiltersGray();
                    findViewById(R.id.b_isbn).setAlpha(1f);

                    handleQuery(barcode.displayValue);

                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    //statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                // statusMessage.setText(String.format(getString(R.string.barcode_error),
                //        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
            Log.i("httpReady", String.valueOf(httpHandler.isReady()));
            if (httpHandler.isReady()) {
                list = httpHandler.getLivros();
                handleQuery(search_input);
                progressBar.setVisibility(View.INVISIBLE);
            }
            else
                new TimeOut().execute("1000");
        }
    }
}
