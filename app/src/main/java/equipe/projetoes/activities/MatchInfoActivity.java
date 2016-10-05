package equipe.projetoes.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import equipe.projetoes.R;
import equipe.projetoes.adapters.MatchInfoRecyclerAdapter;
import equipe.projetoes.data.RestDAO;
import equipe.projetoes.models.Match;
import equipe.projetoes.util.Callback;
import equipe.projetoes.util.Constants;
import equipe.projetoes.util.Global;
import equipe.projetoes.data.LivroDAO;

/**
 * Created by Victor on 20-Sep-16.
 */
public class MatchInfoActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private MatchInfoRecyclerAdapter adapter;
    private RecyclerView mRecyclerView2;
    private MatchInfoRecyclerAdapter adapter2;
    private LivroDAO dao;
    private RestDAO restDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);
        this.init();

        dao = new LivroDAO(this);
        restDAO = RestDAO.getInstance();

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


        // specify an adapter (see also next example)
        adapter = new MatchInfoRecyclerAdapter(Global.lastMatch.getMeusLivros());
        mRecyclerView.setAdapter(adapter);


        mRecyclerView2 = (RecyclerView) findViewById(R.id.recycler2);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setNestedScrollingEnabled(false);

        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView2.setLayoutManager(mLayoutManager2);

        // specify an adapter (see also next example)
        adapter2 = new MatchInfoRecyclerAdapter(Global.lastMatch.getMatchLivros());
        mRecyclerView2.setAdapter(adapter2);

        ((TextView) findViewById(R.id.txt_distancia)).setText(Global.lastMatch.getDistance() + " m");
        findViewById(R.id.bt_trocar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getSelected().isEmpty() || adapter2.getSelected().isEmpty()) {
                    Toast toast = Toast.makeText(v.getContext(), "Por favor, selecione os livros que deseja trocar", Toast.LENGTH_LONG);
                    toast.show();
                } else {

                    restDAO.acceptMatch(Global.lastMatch, new Callback<Integer>() {
                        @Override
                        public void execute(Integer result) {
                            if (result == Constants.REQUEST_SUCESSFUL) {
                                Global.lastMatch = null;
                                finish();
                                //chamar tela de contato ou algo do tipo para comunicação entre os usarios
                            } else if (result == Constants.REQUEST_FAILED) {
                                Toast.makeText(getBaseContext(), getBaseContext().getText(R.string.request_fail),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        findViewById(R.id.bt_rejeitar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restDAO.rejectMatch(Global.lastMatch, new Callback<Integer>() {
                    @Override
                    public void execute(Integer result) {
                        if (result == Constants.REQUEST_SUCESSFUL) {
                            Global.lastMatch = null;
                            finish();
                        } else if (result == Constants.REQUEST_FAILED) {
                            Toast.makeText(getBaseContext(), getBaseContext().getText(R.string.request_fail),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }

}
