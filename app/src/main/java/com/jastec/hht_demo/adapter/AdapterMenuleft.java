package com.jastec.hht_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jastec.hht_demo.R;
import com.jastec.hht_demo.model.PgMenu;

import java.util.ArrayList;
import java.util.List;

public class AdapterMenuleft extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PgMenu> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener onItemClickListener;
    public AdapterMenuleft(Context context, List<PgMenu> items) {
        this.items = items;
        ctx = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
public interface OnItemClickListener {
    void onItemClick(View view, PgMenu obj, int pos);
}
@NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_left, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public ImageView image;
        public TextView tx_pg_name;
        public TextView tx_pg_id;
        public View pg_cardview;



        public ViewHolder(View v) {
            super(v);
            // image = (ImageView) v.findViewById(R.id.image);
            tx_pg_name = (TextView) v.findViewById(R.id.pg_menu_name);
            tx_pg_id = (TextView) v.findViewById(R.id.pg_menu_id);
            pg_cardview = (View) v.findViewById(R.id.pg_cardview);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final PgMenu o = items.get(position);
            view.tx_pg_name.setText(o.pg_name);
            view.tx_pg_id.setText(o.pg_id);
          //  Tools.displayImageRound(ctx, view.image, o.image);

            view.pg_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener == null) return;
                    onItemClickListener.onItemClick(view, o, position);
                }
            });
       }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
