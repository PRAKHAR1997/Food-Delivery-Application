package com.project.gaurs.tadqa.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Adapter_Cart  extends RecyclerView.Adapter<Adapter_Cart.ViewHolder> {

    private List<FoodElements> foodElements;
    Context context;
    DatabaseEntry databaseEntry;

    public Adapter_Cart(List<FoodElements> foodElements, Context context) {
        this.foodElements = foodElements;
        this.context = context;
    }

    @Override
    public Adapter_Cart.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Adapter_Cart.ViewHolder holder, final int position) {
        holder.food_name.setText(foodElements.get(position).getFoodType());
        databaseEntry = new DatabaseEntry(context);
        Glide.with(context.getApplicationContext())
                .load(foodElements.get(position).getPhoto())
                .into(holder.image);
        holder.price.setText(foodElements.get(position).getPrice());
        if(databaseEntry.totalQty()<40){
            holder.add.setEnabled(true);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = foodElements.get(position).getQty()+1;
                foodElements.get(position).setQty(qty);
                databaseEntry = new DatabaseEntry(context);
                databaseEntry.updateInRow(foodElements.get(position).getPhoto(),"cart_table",qty);
                holder.qty.setText(""+foodElements.get(position).getQty());
                AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty()));

                notifyDataSetChanged();
                CartFragment.calculateGrandTotal();
            }
        });
        }else{
            holder.add.setEnabled(false);
        }
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = foodElements.get(position).getQty()-1;
                if(qty>0) {
                    foodElements.get(position).setQty(qty);
                    databaseEntry = new DatabaseEntry(context);
                    databaseEntry.updateInRow(foodElements.get(position).getPhoto(), "cart_table", qty);
                    holder.qty.setText("" + foodElements.get(position).getQty());
                    notifyDataSetChanged();
                    CartFragment.calculateGrandTotal();
                    AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty()));
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseEntry = new DatabaseEntry(context);
                databaseEntry.deleteARow(foodElements.get(position).getPhoto(),"cart_table");
                foodElements.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,foodElements.size());
                CartFragment.calculateGrandTotal();
                AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty()));
            }
        });
        holder.qty.setText(""+foodElements.get(position).getQty());
    }
    @Override
    public int getItemCount() {
        return foodElements.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView food_name, price, qty;
        private ImageView image;
        private ImageButton add, sub;
        private Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.nameofproduct);
            price = (TextView)itemView.findViewById(R.id.priceofproduct);
            image = (ImageView) itemView.findViewById(R.id.imagecart);
            add = (ImageButton)itemView.findViewById(R.id.add1);
            sub = (ImageButton)itemView.findViewById(R.id.sub1);
            qty = (TextView)itemView.findViewById(R.id.quantity);
            delete = (Button)itemView.findViewById(R.id.delete_from_cart);
        }
    }
}
