package com.project.gaurs.tadqa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.project.gaurs.tadqa.Activities.AfterMain;
import com.project.gaurs.tadqa.Pojo.FoodElements;
import com.project.gaurs.tadqa.R;
import com.project.gaurs.tadqa.Unnecessary.RoundedCornersTransformation;

import java.util.List;

/**
 * Created by gaurs on 5/25/2017.
 */

public class Adapter_Menu extends RecyclerView.Adapter<Adapter_Menu.ViewHolder> {

    private List<FoodElements> foodElements;
    Context context;
    ImageLoader imageLoader;

    public Adapter_Menu(Context context, List<FoodElements> foodElements) {
        this.foodElements = foodElements;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(Adapter_Menu.ViewHolder holder, final int position) {
        holder.food_name.setText(foodElements.get(position).getFoodType());
        Glide.with(context.getApplicationContext())
                .load(foodElements.get(position).getPhoto()).bitmapTransform(new RoundedCornersTransformation(context.getApplicationContext(), 5, 0)).into(holder.image);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AfterMain.class);
                intent.putExtra("viewPosition", position);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return foodElements.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView food_name;
        private ImageView image;
        private CardView cardView;
        public ViewHolder(View view) {
            super(view);
            view.setClipToOutline(true);
            food_name = (TextView) view.findViewById(R.id.textview27);
            image = (ImageView) view.findViewById(R.id.imageViewHero);
            cardView = (CardView)view.findViewById(R.id.card);
        }
    }
}
