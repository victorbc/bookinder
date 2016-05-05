package equipe.projetoes.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import equipe.projetoes.R;
import equipe.projetoes.activities.BibliotecaActivity;
import equipe.projetoes.activities.DetalheLivroActivity;
import equipe.projetoes.models.Livro;
import equipe.projetoes.utilis.HolderOnClickListner;
import equipe.projetoes.utilis.LivroDAO;

/**
 * Created by Victor on 4/9/2016.
 */
public class BibliotecaRecyclerAdapter extends RecyclerView.Adapter<BibliotecaRecyclerAdapter.ViewHolder> {
    private List<Livro> mDataset;
    public static final int NOINFO = 0;
    public static final int FULL = 1;
    private int type = 1;
    private Activity act;

    // Provide a suitable constructor (depends on the kind of dataset)
    public BibliotecaRecyclerAdapter(List<Livro> myDataset, int type, Activity act) {
        mDataset = myDataset;
        this.type = type;
        this.act = act;

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

        final String livroNome = mDataset.get(position).getISBN();

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.startActivity(new Intent(act, DetalheLivroActivity.class).putExtra("livroNome", livroNome));
            }
        });

        infoAction.holder = holder;
        holder.btFav.setOnClickListener(infoAction);
        holder.btRead.setOnClickListener(infoAction);
        holder.btTrade.setOnClickListener(infoAction);


        if (livroNome.equals("0")) {
            holder.img.setImageResource(R.drawable.livro);
        } else if (livroNome.equals("1")) {
            holder.img.setImageResource(R.drawable.livro1);

        } else if (livroNome.equals("2")) {
            holder.img.setImageResource(R.drawable.livro2);

        } else if (livroNome.equals("3")) {
            holder.img.setImageResource(R.drawable.livro3);

        }

        if (mDataset.get(position).isFav()) {
            holder.btFav.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.accent), PorterDuff.Mode.SRC_ATOP);
        }
        if (mDataset.get(position).isTradable()) {
            holder.btTrade.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.accent), PorterDuff.Mode.SRC_ATOP);
        }
        holder.btRead.setText(mDataset.get(position).getReadPg() + " / " + mDataset.get(position).getPg());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.img.setImageDrawable(null);
        holder.btFav.setColorFilter(null);
        holder.btTrade.setColorFilter(null);
        holder.btRead.setText("");

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

        }
    }

    public HolderOnClickListner infoAction = new HolderOnClickListner() {

        @Override
        public void onClick(View v) {
            LivroDAO dao = new LivroDAO(act);
            Livro livro = mDataset.get(holder.getAdapterPosition());
            switch (v.getId()) {
                case R.id.bt_fav:
                    if (((ImageView) v).getColorFilter() == null) {
                        ((ImageView) v).setColorFilter(ContextCompat.getColor(act, R.color.accent), PorterDuff.Mode.SRC_ATOP);
                        livro.setFav(true);

                    } else {
                        ((ImageView) v).setColorFilter(null);
                        livro.setFav(false);
                    }
                    break;
                case R.id.bt_read:
                    showPgDialog(livro, dao,holder);
                    break;
                case R.id.bt_trade:
                    if (((ImageView) v).getColorFilter() == null) {
                        ((ImageView) v).setColorFilter(ContextCompat.getColor(act, R.color.accent), PorterDuff.Mode.SRC_ATOP);
                        livro.setTradable(true);
                    } else {
                        ((ImageView) v).setColorFilter(null);
                        livro.setTradable(false);
                    }
                    break;
            }

            dao.atualizaDadosDoLivro(livro);
            ((BibliotecaActivity)act).removeFromTrade(livro);
            ((BibliotecaActivity)act).notifyRecyclers();
        }
    };


    private void showPgDialog(Livro livro, final LivroDAO dao, final RecyclerView.ViewHolder holder) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(act, R.style.AlertDialogTheme);
        LayoutInflater inflater = act.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pg, null);
        final EditText edpg = (EditText) dialogView.findViewById(R.id.edpg);
        TextView pg = (TextView) dialogView.findViewById(R.id.pg);
        String text = livro.getPg()+"";
        pg.setText(text);
        text = livro.getReadPg()+"";
        edpg.setText(text);
        dialogBuilder.setView(dialogView);


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setTitle("Quantidade de páginas lidas");
        dialogView.findViewById(R.id.bt_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataset.get(holder.getAdapterPosition()).setReadPg(Integer.parseInt(edpg.getText().toString()));
                dao.atualizaDadosDoLivro(mDataset.get(holder.getAdapterPosition()));
                ((BibliotecaActivity)act).notifyRecyclers();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }

}