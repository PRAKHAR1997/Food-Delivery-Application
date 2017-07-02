package com.project.gaurs.tadqa.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.project.gaurs.tadqa.Activities.AfterMain;
import com.project.gaurs.tadqa.Fragment.CartFragment;
import com.project.gaurs.tadqa.Pojo.FoodElements;
import com.project.gaurs.tadqa.R;
import com.project.gaurs.tadqa.Unnecessary.DatabaseEntry;

import java.util.List;

/**
 * Created by gaurs on 6/5/2017.
 */

public class Adapter_Fav extends RecyclerView.Adapter<Adapter_Fav.ViewHolder>  {

    private List<FoodElements> foodElements;
    Context context;
    ImageLoader imageLoader;
    DatabaseEntry databaseEntry;

    public Adapter_Fav(List<FoodElements> foodElementsList, Context context) {
        this.context = context;
        this.foodElements = foodElementsList;
    }

    @Override
    public Adapter_Fav.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fav, parent, false);
        return new Adapter_Fav.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Adapter_Fav.ViewHolder holder, final int position) {
        holder.food_name.setText(foodElements.get(position).getFoodType());
        Glide.with(context.getApplicationContext())
                .load(foodElements.get(position).getPhoto())
                .into(holder.image);
        holder.price.setText(foodElements.get(position).getPrice());
        holder.movetocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseEntry = new DatabaseEntry(context);
                databaseEntry.insertIntoCart(foodElements.get(position).getFoodType(),foodElements.get(position).getPhoto(),foodElements.get(position).getPrice(),foodElements.get(position).getRate(), foodElements.get(position).getQty());
                databaseEntry.deleteARow(foodElements.get(position).getPhoto(),"favour_table");
                foodElements.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,foodElements.size());
                CartFragment.calculateGrandTotal();
                AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty()));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseEntry = new DatabaseEntry(context);
                databaseEntry.deleteARow(foodElements.get(position).getPhoto(),"favour_table");
                foodElements.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,foodElements.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodElements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView food_name, price;
        private ImageView image;
        private Button movetocart, delete;


        public ViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.nameproduct);
            price = (TextView)itemView.findViewById(R.id.priceproduct);
            image = (ImageView) itemView.findViewById(R.id.imagefav);
            movetocart = (Button) itemView.findViewById(R.id.button12);
            delete=(Button)itemView.findViewById(R.id.deletebutton);
        }
    }
}
