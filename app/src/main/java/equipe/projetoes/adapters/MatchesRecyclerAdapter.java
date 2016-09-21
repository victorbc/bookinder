package equipe.projetoes.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import equipe.projetoes.R;
import equipe.projetoes.models.Livro;
import equipe.projetoes.models.Match;

/**
 * Created by Victor on 4/9/2016.
 */
public class MatchesRecyclerAdapter extends RecyclerView.Adapter<MatchesRecyclerAdapter.ViewHolder> {
    private List<Match> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MatchesRecyclerAdapter(List<Match> myDataset) {
        mDataset = myDataset;


    }



    public void add(int position, Match item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Match item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MatchesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String desc;

        if (mDataset.get(position).getMeusLivros().size() == 1) {
            desc = "1 livro seu deu match com ";
        } else {
            desc = mDataset.get(position).getMeusLivros().size() + " livros seus deram match com ";
        }

        if (mDataset.get(position).getMatchLivros().size() == 1) {
            desc += "1 livro de " + mDataset.get(position).getMatchName();
        } else {
            desc += mDataset.get(position).getMatchLivros().size() + " livros de " + mDataset.get(position).getMatchName();
        }

        String distance = String.valueOf(mDataset.get(position).getDistance()) + "m";

        holder.txtDesc.setText(desc);
        holder.distance.setText(distance);
        holder.pic1.setBackgroundDrawable(mDataset.get(position).getThumbnail());

        mDataset.get(position).getThumbnail().start();


//        holder.pic1.setImageBitmap(mDataset.get(position).getLivro().getDrawable());
//        holder.pic2.setImageBitmap();

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        if(holder.pic1 != null)
            holder.pic1.setImageDrawable(null);
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

    }

    public List<Match> getmDataset() {
        return mDataset;
    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView txtDesc;
        private TextView distance;
        private ImageView pic1;
        private ImageView pic2;



        public TextView getTxtDesc() {
            return txtDesc;
        }

        public TextView getDistance() {
            return distance;
        }

        public ImageView getPic1() {
            return pic1;
        }
        public ImageView getPic2() {
            return pic2;
        }

        public ViewHolder(View v) {
            super(v);


            txtDesc = (TextView) v.findViewById(R.id.match_phrase);
            distance = (TextView) v.findViewById(R.id.match_distance);
            pic1 = (ImageView) v.findViewById(R.id.match_pic1);
            pic1 = (ImageView) v.findViewById(R.id.match_pic1);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent it = new Intent(v.getContext(),);
                    //v.getContext().startActivity(it);
//                    SelecaoDeSintomasActivity activity = (SelecaoDeSintomasActivity) view.getContext();
//                    activity.openMenuSubMatch(mDataset.get(getAdapterPosition()));

                }
            });
        }
    }



}