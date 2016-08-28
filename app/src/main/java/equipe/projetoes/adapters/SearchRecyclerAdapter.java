package equipe.projetoes.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import equipe.projetoes.R;
import equipe.projetoes.activities.SearchActivity;
import equipe.projetoes.models.Livro;
import equipe.projetoes.utilis.LivroDAO;

/**
 * Created by Victor on 4/9/2016.
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {
    private List<Livro> mDataset;
    private LivroDAO dao;
    private Context ctx;

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchRecyclerAdapter(List<Livro> myDataset, Context ctx) {
        mDataset = myDataset;

        dao = new LivroDAO(ctx);
        this.ctx = ctx;
    }


    public void add(int position, Livro item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Livro item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public SearchRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.txtNome.setText(mDataset.get(position).getNome());
        holder.txtDesc.setText("");
        holder.txtDesc.append(mDataset.get(position).getAutor() + "\n");
        if (mDataset.get(position).getPg() > 0)
            holder.txtDesc.append(mDataset.get(position).getPg() + " páginas");
        else
            holder.txtDesc.append("Número de páginas indisponível");

        String livroNome = mDataset.get(position).getISBN();
        if (holder.img != null) {
            if (livroNome.equals("0")) {
                holder.img.setImageResource(R.drawable.livro);
            } else if (livroNome.equals("1")) {
                holder.img.setImageResource(R.drawable.livro1);

            } else if (livroNome.equals("2")) {
                holder.img.setImageResource(R.drawable.livro2);

            } else if (livroNome.equals("3")) {
                holder.img.setImageResource(R.drawable.livro3);

            }
            //Log.d("test",mDataset.get(position).getNomeCursivo()+ " image id "+mDataset.get(position).getResId());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        if (holder.img != null)
            holder.img.setImageDrawable(null);
        holder.txtDesc.setText("");
        holder.txtNome.setText("");
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView txtNome;
        private TextView txtDesc;
        private ImageView img;
        private View btAdd;


        public ViewHolder(View v) {
            super(v);

            txtNome = (TextView) v.findViewById(R.id.txt_title);
            txtDesc = (TextView) v.findViewById(R.id.txt_info);
            img = (ImageView) v.findViewById(R.id.img);
            btAdd = v.findViewById(R.id.bt_add);

            btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dao.adiciona(mDataset.get(getAdapterPosition()));
                    Toast.makeText(ctx, "Livro adicionado a biblioteca", Toast.LENGTH_SHORT);
                }
            });
        }
    }

    public List<Livro> getmDataset() {
        return mDataset;
    }
}