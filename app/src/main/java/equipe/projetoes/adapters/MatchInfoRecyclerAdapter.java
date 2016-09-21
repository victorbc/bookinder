package equipe.projetoes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import equipe.projetoes.R;
import equipe.projetoes.models.Livro;

/**
 * Created by Victor on 4/9/2016.
 */
public class MatchInfoRecyclerAdapter extends RecyclerView.Adapter<MatchInfoRecyclerAdapter.ViewHolder> {
    private List<Livro> mDataset;
    private List<Livro> selected;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MatchInfoRecyclerAdapter(List<Livro> myDataset) {
        mDataset = myDataset;
        selected = new ArrayList<Livro>();

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
    public MatchInfoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_livro_check, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (mDataset.get(position).getDrawable() != null)
            holder.img.setImageBitmap(mDataset.get(position).getDrawable());
        else
            holder.img.setImageResource(R.drawable.noimage);

        holder.select.setVisibility(View.GONE);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.select.getVisibility() == View.VISIBLE) {
                    holder.select.setVisibility(View.GONE);
                    selected.remove(selected.indexOf(mDataset.get(position)));
                } else {
                    holder.select.setVisibility(View.VISIBLE);
                    selected.add(mDataset.get(position));
                }
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.img.setImageDrawable(null);
        holder.select.setVisibility(View.GONE);


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
        public ImageView img;
        private View select;

        public ViewHolder(View v) {
            super(v);
            img = (ImageView) v.findViewById(R.id.livro);
            select = v.findViewById(R.id.select);

        }
    }


    public List<Livro> getSelected(){
        return selected;
    }

}