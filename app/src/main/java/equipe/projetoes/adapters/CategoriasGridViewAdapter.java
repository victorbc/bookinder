package equipe.projetoes.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import equipe.projetoes.models.Categoria;
import equipe.projetoes.R;

/**
 * Created by Nicolas on 17/03/2016.
 */
public class CategoriasGridViewAdapter extends BaseAdapter {
        private Activity mContext;
        private List<Categoria> categorias;
         private LayoutInflater mInflater;

        public CategoriasGridViewAdapter(Activity c ,List<Categoria> categorias) {
            this.categorias = categorias;
            mInflater = LayoutInflater.from(c);
            mContext = c;
        }

        public int getCount() {
            return categorias.size();
        }

        public Object getItem(int position) {
            return categorias.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.item_categoria, null);
            TextView nome = (TextView) convertView.findViewById(R.id.nome_categoria);
            ImageView img = (ImageView) convertView.findViewById(R.id.imagem_categoria);

            Categoria categoria = categorias.get(position);
            nome.setText(categoria.getNome());
            img.setImageResource(categoria.getImagem(mContext));
            return convertView;
        }


    }

