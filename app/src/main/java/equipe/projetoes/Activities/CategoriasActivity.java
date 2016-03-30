package equipe.projetoes.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.ShapeImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import equipe.projetoes.Adapters.CategoriasGridViewAdapter;
import equipe.projetoes.Models.Categoria;
import equipe.projetoes.R;

public class CategoriasActivity extends AppCompatActivity{
    private static List<Categoria> mCategorias;
    private List<Categoria> isCategoriaSelecionada = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        mCategorias = Arrays.asList(Categoria.values());
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        GridView gridview = (GridView) findViewById(R.id.itens_categorias);
        gridview.setAdapter(new CategoriasGridViewAdapter(this, mCategorias));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(CategoriasActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();

                TextView nome = (TextView) v.findViewById(R.id.nome_categoria);
                CircularImageView img = (CircularImageView) v.findViewById(R.id.imagem_categoria);

                if(!isCategoriaSelecionada.contains(mCategorias.get(position))) {
                    nome.setTextAppearance(getApplicationContext(),R.style.boldTextVerde);
                    img.setBorderColor(getResources().getColor(R.color.green));
                    isCategoriaSelecionada.add(mCategorias.get(position));
                }else{
                    nome.setTextAppearance(getApplicationContext(), R.style.normalText);
                    img.setBorderColor(getResources().getColor(R.color.branco));
                    isCategoriaSelecionada.remove(mCategorias.get(position));
                }

            }
        });

    }



}
