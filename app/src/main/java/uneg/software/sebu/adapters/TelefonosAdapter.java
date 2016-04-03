package uneg.software.sebu.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uneg.software.sebu.R;
import uneg.software.sebu.fragments.TelefonoDialogFragment;
import uneg.software.sebu.interfaces.RecyclerItemClickListener;
import uneg.software.sebu.fragments.TelefonoDialogFragment;
import uneg.software.sebu.models.bd.Telefono;
import uneg.software.sebu.views.CustomFontTextView;

/**
 * Created by Jhonny on 18/8/2015.
 */
public class TelefonosAdapter extends RecyclerView.Adapter<TelefonosAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Telefono> lista;
    private final Activity activity;
    private final RecyclerItemClickListener clickListener;

    public TelefonosAdapter(Activity activity, List<Telefono> lista, TelefonoDialogFragment f) {
        this.clickListener=f;
        this.inflater = LayoutInflater.from(activity);
        this.lista = lista;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_telefono, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Telefono itm = lista.get(position);
        holder.setItem(itm);
        holder.telefono.setText(itm.getTelefono());
        holder.itemView.setTag(itm);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @InjectView(R.id.telefono)
        CustomFontTextView telefono;
        @InjectView(R.id.eliminar_telefono)
        ImageView eliminarTelefono;

        private Telefono item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setItem(Telefono item) {
            this.item = item;
        }

        public Telefono getItem() {
            return item;
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(item);
        }
    }

}
