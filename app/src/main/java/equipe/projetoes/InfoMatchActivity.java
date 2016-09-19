package equipe.projetoes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import equipe.projetoes.controllers.MatchController;
import equipe.projetoes.models.Match;

public class InfoMatchActivity extends AppCompatActivity {

    private Match match;
    private Button buttonRemove;
    private TextView nome;
    private MatchController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_match);

        mController = MatchController.getInstance();

        Bundle bundle = getIntent().getExtras();
        match = (Match)bundle.get("match");
        buttonRemove = (Button)findViewById(R.id.button);
        nome = (TextView)findViewById(R.id.tvNomeLivro);
        nome.setText(match.getBookName());

        buttonRemove.setText("Testando123 " + match.getBookName());
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // como eu pego o match que eu cliquei pra ver as informacoes?
                //remover ele do array q eh passado pro adapter

                mController.remove(match);
                finish();
                //Intent it = new Intent(InfoMatchActivity.this, MatchListActivity.class);
                //startActivity(it);
            }
        });
    }

}
