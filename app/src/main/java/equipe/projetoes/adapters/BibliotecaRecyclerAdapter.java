package equipe.projetoes.adapters;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import equipe.projetoes.R;
import equipe.projetoes.activities.DetalheLivroActivity;
import equipe.projetoes.models.Livro;

/**
 * Created by Victor on 4/9/2016.
 */
public class BibliotecaRecyclerAdapter extends RecyclerView.Adapter<BibliotecaRecyclerAdapter.ViewHolder> {
    private List<Livro> mDataset;
    public static final int NOINFO = 0;
    public static final int FULL = 1;
    private int type = 1;

    // Provide a suitable constructor (depends on the kind of dataset)
    public BibliotecaRecyclerAdapter(List<Livro> myDataset, int type) {
        mDataset = myDataset;
        this.type = type;


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
    public BibliotecaRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_livro, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // holder.txtNome.setText(mDataset.get(position).getNomeCursivo());
        //holder.img.setImageResource(mDataset.get(position).getResId());
        //Log.d("test",mDataset.get(position).getNomeCursivo()+ " image id "+mDataset.get(position).getResId());
        holder.img.setImageResource(mDataset.get(position).getResId());
        if (type == NOINFO)
            holder.info.setVisibility(View.GONE);
        if (type == FULL)
            holder.info.setVisibility(View.VISIBLE);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.img.setImageDrawable(null);
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
        public View info;
        public ImageView img;
        private ImageView btFav;
        private TextView btRead;
        private ImageView btTrade;

        public ViewHolder(View v) {
            super(v);
            info = v.findViewById(R.id.menu_info);
            img = (ImageView) v.findViewById(R.id.livro);

            btFav = (ImageView) v.findViewById(R.id.bt_fav);
            btRead = (TextView) v.findViewById(R.id.bt_read);
            btTrade = (ImageView) v.findViewById(R.id.bt_trade);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(v.getContext(), DetalheLivroActivity.class).putExtra("livroNome",mDataset.get(getAdapterPosition()).getNome()));
                }
            });
            btFav.setOnClickListener(infoAction);
            btRead.setOnClickListener(infoAction);
            btTrade.setOnClickListener(infoAction);
          /*  txtNome = (TextView) v.findViewById(R.id.txt_name);
            txtDesc = (TextView) v.findViewById(R.id.txt_desc);
            img = (ImageView) v.findViewById(R.id.img);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent it = new Intent(v.getContext(),);
                    //v.getContext().startActivity(it);
                    SelecaoDeSintomasActivity activity = (SelecaoDeSintomasActivity) view.getContext();
                    activity.openMenuSubMatch(mDataset.get(getAdapterPosition()));

                }
            });*/
        }
    }

    public View.OnClickListener infoAction = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_fav:
                    if (((ImageView) v).getColorFilter() == null)
                        ((ImageView) v).setColorFilter(ContextCompat.getColor(v.getContext(), R.color.accent), PorterDuff.Mode.SRC_ATOP);
                    else
                        ((ImageView) v).setColorFilter(null);
                    break;
                case R.id.bt_read:
                    break;
                case R.id.bt_trade:
                    if (((ImageView) v).getColorFilter() == null)
                    ((ImageView) v).setColorFilter(ContextCompat.getColor(v.getContext(), R.color.accent), PorterDuff.Mode.SRC_ATOP);
                    else
                        ((ImageView) v).setColorFilter(null);
                    break;
            }
        }
    };

}