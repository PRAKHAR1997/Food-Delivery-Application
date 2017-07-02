package com.project.gaurs.tadqa.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.gaurs.tadqa.Activities.AfterMain;
import com.project.gaurs.tadqa.Pojo.FoodElements;
import com.project.gaurs.tadqa.R;
import com.project.gaurs.tadqa.Unnecessary.DatabaseEntry;
import com.project.gaurs.tadqa.Unnecessary.RoundedCornersTransformation;

import java.util.List;

/**
 * Created by gaurs on 5/31/2017.
 */

public class Adapter_Menu_Items extends RecyclerView.Adapter<Adapter_Menu_Items.ViewHolder> {

    private List<FoodElements> foodElements;
    Context context;
    boolean isPressed = false;
    int rate = 0;
    DatabaseEntry databaseEntry;

        public Adapter_Menu_Items(Context context, List<FoodElements> foodElements) {
        this.foodElements = foodElements;
        this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_menu, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final Adapter_Menu_Items.ViewHolder holder, final int position) {
            databaseEntry = new DatabaseEntry(context);
            holder.food_name.setText(foodElements.get(position).getFoodType());
            Glide.with(context.getApplicationContext())
                    .load(foodElements.get(position).getPhoto())
                    .bitmapTransform(new RoundedCornersTransformation(context.getApplicationContext(), 15, 1))
                    .into(holder.image);
            holder.price.setText(foodElements.get(position).getPrice());
            if(databaseEntry.totalQty()<40) {
                holder.add.setEnabled(true);
                holder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseEntry = new DatabaseEntry(context);
                        rate = foodElements.get(position).getRate();
                        databaseEntry.insertIntoCart(foodElements.get(position).getFoodType(), foodElements.get(position).getPhoto(), foodElements.get(position).getPrice(), rate, 1);
                        Toast.makeText(context, "Food Added to Cart.", Toast.LENGTH_SHORT);
                        AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty()));
                    }
                });
            }else{
                holder.add.setEnabled(false);

            }
            holder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!isPressed) {
                        holder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        databaseEntry = new DatabaseEntry(context);
                        databaseEntry.insertIntoFav(foodElements.get(position).getFoodType(), foodElements.get(position).getPhoto(), foodElements.get(position).getPrice(), rate, 1);
                        Toast.makeText(context, "Food Added to Favourites.", Toast.LENGTH_SHORT);
                    }else if(isPressed){
                        databaseEntry = new DatabaseEntry(context);
                        databaseEntry.deleteARow(foodElements.get(position).getPhoto(), "favour_table");
                        holder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                    isPressed = !isPressed;
                }
            });
        }
        @Override
        public int getItemCount() {
        return foodElements.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView food_name, price;
            private ImageView image;
            private ImageButton add, fav;

            public ViewHolder(View view) {
                super(view);
                food_name = (TextView) view.findViewById(R.id.menuItemName);
                image = (ImageView) view.findViewById(R.id.dishimage);
                price = (TextView)view.findViewById(R.id.priceofproduct);
                add = (ImageButton)view.findViewById(R.id.imageButton3);
                fav = (ImageButton)view.findViewById(R.id.imageButton4);
            }
        }
    }
