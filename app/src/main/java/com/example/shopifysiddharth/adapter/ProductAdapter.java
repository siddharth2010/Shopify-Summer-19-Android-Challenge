package com.example.shopifysiddharth.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopifysiddharth.CollectionActivity;
import com.example.shopifysiddharth.ProductActivity;
import com.example.shopifysiddharth.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<String> coll;
    private List<String> nn;
    private Context mContext;

    public ProductAdapter(Context applicationContext, ArrayList<String> product, ArrayList<String> nok) {
        this.coll = product;
        this.nn = nok;
        mContext = applicationContext;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // notifyDataSetChanged();
        String s = CollectionActivity.movieList.get(CollectionAdapter.k);
        String p = CollectionActivity.img.get(CollectionAdapter.k);
        String c = ProductActivity.pname.get(position);
        String u = String.valueOf(ProductActivity.quanty.get(position));
        //holder.title.setText(s);
        List<String> hh = Arrays.asList(TextUtils.split(c, " "));
        int k = hh.size();
        List<String> hhh = Arrays.asList("");
        hhh = new ArrayList<>(hhh);;
        //Log.d("iii" , " ---------     " +    hh.get(1));
        for (int ba = 1; ba < k;ba++ ){
            Log.d("iii" , " ---------     " +    hh.get(ba));
            hhh.add(hh.get(ba));
        }
        c = TextUtils.join(" ",hhh);

        // c = hhh.remove(0);

        u = "Units Available " + u;
        holder.prod.setText(u);
        holder.title.setText(c);
        Picasso.get().load(p).into(holder.ppl);
        holder.unit.setText(s);
    }


    @Override
    public int getItemCount() {
        return coll.size();
    }


    public class MyViewHolder  extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView prod;
        public ImageView ppl;
        public TextView unit;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tle);
            ppl = itemView.findViewById(R.id.pic);
            prod = itemView.findViewById(R.id.secondry);
            unit = itemView.findViewById(R.id.third);

        }
    }
}
